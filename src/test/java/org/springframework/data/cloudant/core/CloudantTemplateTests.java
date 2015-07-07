package org.springframework.data.cloudant.core;

/**
 * Created by justinsaul on 6/10/15.
 */

import com.cloudant.client.api.CloudantClient;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.lightcouch.DocumentConflictException;
import org.lightcouch.NoDocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.cloudant.TestSpringBootApplication;
import org.springframework.data.cloudant.core.model.BaseDocument;
import org.springframework.data.cloudant.core.model.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestSpringBootApplication.class)
@TestExecutionListeners(CloudantTemplateViewListener.class)
public class CloudantTemplateTests {

    @Autowired
    private CloudantTemplate template;


    @Test
    public void saveSimpleEntityCorrectly() throws Exception {
        String id = "cars:fast-car";
        String name = "The Fast Car";
        tryRemoveObjectById(id, Car.class);

        boolean running = false;
        Car car = new Car(id).setName(name).setRunning(running);

        template.save(car);
        car = (Car) template.findById(id, Car.class);
        assertNotNull(car);
        assertEquals(false, car.getRunnning());
        assertEquals(name, car.getName());
        assertEquals(id, car.getId());
    }

    @Test
    public void saveUnMappedDataAdapter() throws Exception {
        String id = "cars:fast-car";
        String name = "The Fast Car";
        tryRemoveObjectById(id, Car.class);

        boolean running = false;
        Car car = new Car(id).setName(name).setRunning(running);
        HashMap<String, Object> unmap = new HashMap<String, Object>();
        unmap.put("TESTING", "VALUE");
        car.setUnmappedFields(unmap);
        template.setUnmappedDataAdapter();

        template.save(car);

        car = (Car) template.findById(id, Car.class);
        assertNotNull(car);
        assertEquals(unmap.size(), car.getUnmappedFields().size());
        assertEquals("VALUE", car.getUnmappedFields().get("TESTING").toString());
    }


    private void tryRemoveObjectById(String id, Class<? extends BaseDocument> entityClass) {
        try {
            BaseDocument doc = template.findById(id, entityClass);
            System.out.println("Class:" + entityClass.getSimpleName() + " ID:" + doc.getId() + " Rev:" + doc.getRevision());
            template.remove(doc);
        } catch (NoDocumentException exception) {
        }
    }

    private void tryRemoveObject(BaseDocument entity) {
        tryRemoveObjectById(entity.getId(), entity.getClass());
    }

    private void trySaveObject(BaseDocument entity) {
        try {
            System.out.println("ID:" + entity.getId() + " class:" + entity.getClass().getName());
            BaseDocument doc = template.findById(entity.getId(), entity.getClass());
        } catch (NoDocumentException exception) {
            template.save(entity);
        }
    }


    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void insertDoesNotOverride() throws Exception {
        String id = "double-insert-test";
        tryRemoveObjectById(id, SimplePerson.class);
        SimplePerson doc = new SimplePerson(id, "Mr. A");
        template.save(doc);
        SimplePerson result = (SimplePerson) template.findById(id, SimplePerson.class);
        assertEquals("Mr. A", result.getName());
        doc = new SimplePerson(id, "Mr. B");
        thrown.expect(DocumentConflictException.class);
        template.save(doc);
        result = (SimplePerson) template.findById(id, SimplePerson.class);

        assertEquals("Mr. A", result.getName());
    }


    @Test
    public void updateDoesNotInsert() {
        String id = "update-does-not-insert";
        tryRemoveObjectById(id, SimplePerson.class);
        SimplePerson doc = new SimplePerson(id, "Nice Guy");
        doc.setRevision("1-ssss");
        thrown.expect(DocumentConflictException.class);
        template.update(doc);
        doc = (SimplePerson) template.findById(id, SimplePerson.class);
        assertNull(doc);
    }


    @Test
    public void removeDocument() {
        String id = "cars:to-delete-stout";
        Car car = new Car(id);
        tryRemoveObject(car);
        template.save(car);
        car = (Car) template.findById(id, Car.class);
        assertNotNull(car);

        template.remove(car);
        thrown.expect(NoDocumentException.class);
        car = (Car) template.findById(id, Car.class);
        assertNull(car);
    }


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
        tryRemoveObject(complex);
        template.save(complex);
        ComplexPerson complexPerson = (ComplexPerson) template.findById(id, ComplexPerson.class);
        assertNotNull(complexPerson);
        assertEquals(names, complexPerson.getFirstnames());
        assertEquals(votes, complexPerson.getVotes());
        assertEquals(id, complexPerson.getId());
        assertEquals(info1, complexPerson.getInfo1());
        assertEquals(info2, complexPerson.getInfo2());
    }

    @Test
    public void validFindById() {
        String id = "cars:findme-stout";
        //temp commenting out so we get the doc with extra data.
        String name = "The Findme Stout";
        boolean active = true;
        Car car = new Car(id).setName(name).setActive(active);
        tryRemoveObject(car);
        template.save(car);

        Car found = (Car) template.findById(id, Car.class);

        assertNotNull(found);
        assertEquals(id, found.getId());
        assertEquals(name, found.getName());
        assertEquals(active, found.isActive());
    }

//    @Test
//    public void shouldLoadAndMapViewDocs() {
////        Query query = new Query();
////        query.setStale(Stale.FALSE);
//
//        final List<Car> cars = template.findByView("test_cars", "by_name", query, car.class);
//        assertTrue(cars.size() > 0);
//
//        for (Car car : cars) {
//            assertNotNull(car.getId());
//            assertNotNull(car.getName());
//            assertNotNull(car.getActive());
//        }
//    }

    @Test
    public void shouldDeserialiseLongs() {
        final long time = new Date().getTime();
        SimpleWithLong simpleWithLong = new SimpleWithLong("simpleWithLong:simple", time);
        tryRemoveObject(simpleWithLong);
        template.save(simpleWithLong);
        simpleWithLong = (SimpleWithLong)template.findById("simpleWithLong:simple", SimpleWithLong.class);
        assertNotNull(simpleWithLong);
        assertEquals(time, simpleWithLong.getValue());
    }

    @Test
    public void shouldDeserialiseEnums() {
        SimpleWithEnum simpleWithEnum = new SimpleWithEnum("simpleWithEnum:enum", SimpleWithEnum.Type.BIG);
        tryRemoveObject(simpleWithEnum);
        template.save(simpleWithEnum);
        simpleWithEnum = (SimpleWithEnum)template.findById("simpleWithEnum:enum", SimpleWithEnum.class);
        assertNotNull(simpleWithEnum);
        assertEquals(simpleWithEnum.getType(), SimpleWithEnum.Type.BIG);
    }

    //    @Test
//    public void shouldDeserialiseClass() {
//        SimpleWithClass simpleWithClass = new SimpleWithClass("simpleWithClass:class", Integer.class);
//        simpleWithClass.setValue("The dish ran away with the spoon.");
//        tryRemoveObject(simpleWithClass);
//        template.save(simpleWithClass);
//        simpleWithClass = template.findById("simpleWithClass:class", SimpleWithClass.class);
//        assertNotNull(simpleWithClass);
//        assertThat(simpleWithClass.getValue(), equalTo("The dish ran away with the spoon."));
//    }
//
    @Test
    public void shouldHandleCASVersionOnInsert() throws Exception {
        String id = "versionedClass:1";
        tryRemoveObjectById(id, VersionedClass.class);
        VersionedClass versionedClass = new VersionedClass("versionedClass:1", "foobar");
        assertEquals(0, versionedClass.getVersion());
//        template.insert(versionedClass);
//        CASValue<Object> rawStored = template.findByIds("versionedClass:1");
//        assertEquals(rawStored.getCas(), versionedClass.getVersion());
    }

    @Test
    public void versionShouldNotUpdateOnSecondInsert() throws Exception {
        String id = "versionedClass:2";
        tryRemoveObjectById(id, VersionedClass.class);
        VersionedClass versionedClass = new VersionedClass("versionedClass:2", "foobar");
//        template.insert(versionedClass);
//        long version1 = versionedClass.getVersion();
//        template.insert(versionedClass);
//        long version2 = versionedClass.getVersion();
//
//        assertTrue(version1 > 0);
//        assertTrue(version2 > 0);
//        assertEquals(version1, version2);
    }
//
//    @Test
//    public void shouldSaveDocumentOnMatchingVersion() throws Exception {
//        client.delete("versionedClass:3").get();
//
//        VersionedClass versionedClass = new VersionedClass("versionedClass:3", "foobar");
//        template.insert(versionedClass);
//        long version1 = versionedClass.getVersion();
//
//        versionedClass.setField("foobar2");
//        template.save(versionedClass);
//        long version2 = versionedClass.getVersion();
//
//        assertTrue(version1 > 0);
//        assertTrue(version2 > 0);
//        assertNotEquals(version1, version2);
//
//        assertEquals("foobar2", template.findById("versionedClass:3", VersionedClass.class).getField());
//    }
//
//    @Test(expected = OptimisticLockingFailureException.class)
//    public void shouldNotSaveDocumentOnNotMatchingVersion() throws Exception {
//        client.delete("versionedClass:4").get();
//
//        VersionedClass versionedClass = new VersionedClass("versionedClass:4", "foobar");
//        template.insert(versionedClass);
//
//        assertTrue(client.set("versionedClass:4", "different").get());
//
//        versionedClass.setField("foobar2");
//        template.save(versionedClass);
//    }
//
//    @Test
//    public void shouldUpdateDocumentOnMatchingVersion() throws Exception {
//        client.delete("versionedClass:5").get();
//
//        VersionedClass versionedClass = new VersionedClass("versionedClass:5", "foobar");
//        template.insert(versionedClass);
//        long version1 = versionedClass.getVersion();
//
//        versionedClass.setField("foobar2");
//        template.update(versionedClass);
//        long version2 = versionedClass.getVersion();
//
//        assertTrue(version1 > 0);
//        assertTrue(version2 > 0);
//        assertNotEquals(version1, version2);
//
//        assertEquals("foobar2", template.findById("versionedClass:5", VersionedClass.class).getField());
//    }
//
//    @Test(expected = OptimisticLockingFailureException.class)
//    public void shouldNotUpdateDocumentOnNotMatchingVersion() throws Exception {
//        client.delete("versionedClass:6").get();
//
//        VersionedClass versionedClass = new VersionedClass("versionedClass:6", "foobar");
//        template.insert(versionedClass);
//
//        assertTrue(client.set("versionedClass:6", "different").get());
//
//        versionedClass.setField("foobar2");
//        template.update(versionedClass);
//    }
//
//    @Test
//    public void shouldLoadVersionPropertyOnFind() throws Exception {
//        client.delete("versionedClass:7").get();
//
//        VersionedClass versionedClass = new VersionedClass("versionedClass:7", "foobar");
//        template.insert(versionedClass);
//        assertTrue(versionedClass.getVersion() > 0);
//
//        VersionedClass foundClass = template.findById("versionedClass:7", VersionedClass.class);
//        assertEquals(versionedClass.getVersion(), foundClass.getVersion());
//    }


}