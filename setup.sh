#!/usr/bin/env bash

gradle idea
cp src/test/resources/application.yml.example src/test/resources/application.yml
cp src/test/resources/test-context.xml.example src/test/resources/test-context.xml
