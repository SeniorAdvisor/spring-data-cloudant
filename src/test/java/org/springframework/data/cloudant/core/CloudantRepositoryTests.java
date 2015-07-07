package org.springframework.data.cloudant.core;

/**
* Created by justinsaul on 6/10/15.
*/

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.lightcouch.DocumentConflictException;
import org.lightcouch.NoDocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.cloudant.TestSpringBootApplication;
import org.springframework.data.cloudant.core.model.*;
import org.springframework.data.cloudant.core.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

import static org.junit.Assert.*;
import static org.lightcouch.internal.CouchDbUtil.JsonToObject;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestSpringBootApplication.class)
@TestExecutionListeners(CloudantTemplateViewListener.class)
public class CloudantRepositoryTests {

    @Autowired
    private CarRepository carRepository;


    @Test
    public void saveSimpleEntityCorrectly() throws Exception {
        String id = "cars:fast-car1";
        String name = "The Fast Car";
       tryRemoveObjectById(id, carRepository);

        boolean running = false;
        Car car = new Car(id).setName(name).setRunning(running);

        carRepository.save(car);
        car = carRepository.findOne(id);
        assertNotNull(car);
        assertEquals(false, car.getRunnning());
        assertEquals(name, car.getName());
        assertEquals(id, car.getId());
    }

    private void tryRemoveObjectById(String id,CloudantCrudRepository repository) {
        try {
            BaseDocument doc = repository.findOne(id);
            System.out.println("Class:" +repository.getClass().getSimpleName() + " ID:" + doc.getId() + " Rev:" + doc.getRevision());
            repository.delete(doc);
        } catch (NoDocumentException exception) {
        }
    }

    private void tryRemoveObject(BaseDocument entity,CloudantCrudRepository repository) {
        tryRemoveObjectById(entity.getId(),repository);
    }

    private void trySaveObject(BaseDocument entity,CloudantCrudRepository repository) {
        try {
            System.out.println("ID:" + entity.getId() + " class:" + entity.getClass().getName());
            BaseDocument doc = repository.findOne(entity.getId());
        } catch (NoDocumentException exception) {
            repository.save(entity);
        }
    }


    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private SimplePersonRepository simplePersonRepository;

    @Test
    public void insertDoesNotOverride() throws Exception {
        String id = "double-insert-test";
        tryRemoveObjectById(id, simplePersonRepository);
        SimplePerson doc = new SimplePerson(id, "Mr. A");
        simplePersonRepository.save(doc);
        SimplePerson result = simplePersonRepository.findOne(id);
        assertEquals("Mr. A", result.getName());
        doc = new SimplePerson(id, "Mr. B");
        thrown.expect(DocumentConflictException.class);
        simplePersonRepository.save(doc);
        result = simplePersonRepository.findOne(id);

        assertEquals("Mr. A", result.getName());
    }

    @Test
    public void updateDoesNotInsert() {
        String id = "update-does-not-insert";
        tryRemoveObjectById(id, simplePersonRepository);
        SimplePerson doc = new SimplePerson(id, "Nice Guy");
        doc.setRevision("1-ssss");
        thrown.expect(DocumentConflictException.class);
        simplePersonRepository.update(doc);
        doc = simplePersonRepository.findOne(id);
        assertNull(doc);
    }

    @Test
    public void removeDocument() {
        String id = "cars:to-delete-stout";
        Car car = new Car(id);
        tryRemoveObject(car,carRepository);
        carRepository.save(car);
        car = carRepository.findOne(id);
        assertNotNull(car);

        carRepository.delete(car);
        thrown.expect(NoDocumentException.class);
        car = carRepository.findById(id);
        assertNull(car);
    }

    @Autowired
    private ComplexPersonRepository complexPersonRepository;

    @Test
    public void storeListsAndMaps() {
        String id = "persons:lots-of-names";
        List<String> names = new ArrayList<String>();
        names.add("Michael");
        names.add("Thomas");
        names.add(null);
        List<Integer> votes = new LinkedList<Integer>();
        Map<String, Boolean> info1 = new HashMap<String, Boolean>();
        info1.put("foo", true);
        info1.put("bar", false);
//        info1.put("nullValue", null);
        Map<String, Integer> info2 = new HashMap<String, Integer>();

        ComplexPerson complex = new ComplexPerson(id, names, votes, info1, info2);
        tryRemoveObject(complex,complexPersonRepository);
        complexPersonRepository.save(complex);
        ComplexPerson response = complexPersonRepository.findOne(id);
        assertNotNull(response);
        assertEquals(names, response.getFirstnames());
        assertEquals(votes, response.getVotes());
        assertEquals(id, response.getId());
        assertEquals(info1, response.getInfo1());
        assertEquals(info2, response.getInfo2());
    }

    @Test
    public void validFindById() {
        String id = "cars:findme-stout";
        String name = "The Findme Stout";
        boolean active = true;
        Car car = new Car(id).setName(name).setActive(active);
        tryRemoveObject(car,carRepository);
        carRepository.save(car);

        Car found = carRepository.findOne(id);

        assertNotNull(found);
        assertEquals(id, found.getId());
        assertEquals(name, found.getName());
        assertEquals(active, found.isActive());
    }

    @Test
    public void findAllDocs() {
        String id = "carsfindme-stout";
        String name = "The Findme Stout";
        boolean active = true;
        Car car = new Car(id).setName(name).setActive(active);
        tryRemoveObject(car, carRepository);
        carRepository.save(car);
        List<Car> result = (List<Car>)carRepository.findAll();
        assertNotEquals(0, result.size());
    }

    @Test
    public void findAndSort() {
        String id = "carsfindme-stout";
        String name = "The Findme Stout";
        boolean active = true;
        Car car = new Car(id).setName(name).setActive(active);
        tryRemoveObject(car, carRepository);
        carRepository.save(car);
        Sort sort = new Sort(Sort.Direction.DESC, "created_at");
        List<Car> result = (List<Car>)carRepository.findAll(sort);
        assertNotEquals(0, result.size());
    }

    @Test
    public void findAllDocsWithPage() {
        String id = "carsfindme-stout";
        String name = "The Findme Stout";
        boolean active = true;
        Car car = new Car(id).setName(name).setActive(active);
        tryRemoveObject(car, carRepository);
        carRepository.save(car);

        PageRequest pageable = new PageRequest(0, 10);
        Page<Car> result = carRepository.findAll(pageable);
        assertNotEquals(0, result.getNumberOfElements());
    }

    @Autowired
    private SimpleWithLongRepository simpleWithLongRepository;
    @Test
    public void shouldDeserialiseLongs() {
        final long time = new Date().getTime();
        SimpleWithLong simpleWithLong = new SimpleWithLong("simpleWithLong:simple", time);
        tryRemoveObject(simpleWithLong,simpleWithLongRepository);
        simpleWithLongRepository.save(simpleWithLong);
        simpleWithLong = simpleWithLongRepository.findOne("simpleWithLong:simple");
        assertNotNull(simpleWithLong);
        assertEquals(time, simpleWithLong.getValue());
    }

    @Autowired
    private SimpleWithEnumRepository simpleWithEnumRepository;

    @Test
    public void shouldDeserialiseEnums() {
        SimpleWithEnum simpleWithEnum = new SimpleWithEnum("simpleWithEnum:enum", SimpleWithEnum.Type.BIG);
        tryRemoveObject(simpleWithEnum,simpleWithEnumRepository);
        simpleWithEnumRepository.save(simpleWithEnum);
        simpleWithEnum = simpleWithEnumRepository.findOne("simpleWithEnum:enum");
        assertNotNull(simpleWithEnum);
        assertEquals(simpleWithEnum.getType(), SimpleWithEnum.Type.BIG);
    }

    @Test
    public void shouldKeepUnmappedFields() {
        String id = "test-car";
        tryRemoveObjectById(id, carRepository);
        Car car = new Car(id);
        car.setName("Testing car");
        car = carRepository.save(car);
        HashMap<String, Object> unmap = new HashMap<String, Object>();
        unmap.put("TESTING", "VALUE");
        car.setUnmappedFields(unmap);
        carRepository.update(car);
        Car carExtra = carRepository.findOne(id);
        assertEquals(unmap.size(),carExtra.getUnmappedFields().size());
    }
}