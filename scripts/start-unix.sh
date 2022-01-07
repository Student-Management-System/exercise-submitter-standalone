#!/bin/sh
cd "$(dirname "$0")"
java --illegal-access=deny -jar ExerciseSubmitter.jar
