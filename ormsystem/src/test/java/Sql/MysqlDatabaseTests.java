package Sql;

import Entity.TestEntity;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MysqlDatabaseTests {

    static MysqlDatabase table = new MysqlDatabase();

    @BeforeAll
    static void beforeAll() {
        table.createTable(TestEntity.class);
    }

    @AfterAll
    static void afterAll() {
        table.dropTable(TestEntity.class);
    }

    @AfterEach
    void afterEach() {
        table.deleteEntireTable(TestEntity.class);
    }

    @Test
    void select_getListOfObjects_ExpectsToBeEmpty() {
        List<TestEntity> list = table.findAll(TestEntity.class);
        assertEquals(0, list.size());
    }

    @Test
    void select_findOneInsertObject_ExpectsToBeSize1() {
        TestEntity test = TestEntity.createRandomTest();
        table.createOne(test);
        List<TestEntity> list = table.findOne(TestEntity.class, "id", test.getId());
        assertEquals(1, list.size());
    }

    @Test
    void select_FindOneInsertingTwoObjects_ExpectOneObject() {
        TestEntity test1 = TestEntity.createRandomTest();
        TestEntity test2 = TestEntity.createRandomTest();
        String name = "shai";
        test1.setFirstName(name);
        test2.setFirstName(name);
        List<TestEntity> testList = new ArrayList<>();
        testList.add(test1);
        testList.add(test2);
        table.createMany(testList);
        List<TestEntity> insertedTests = new ArrayList<>();
        assertEquals(table.findOne(TestEntity.class, "firstName", name).size(), 1);
    }

    @Test
    void select_FindAnyInsertedObjects_ExpectMultipleObjects() {
        TestEntity test1 = TestEntity.createRandomTest();
        TestEntity test2 = TestEntity.createRandomTest();
        String name = "shai";
        test1.setFirstName(name);
        test2.setFirstName(name);
        List<TestEntity> testList = new ArrayList<>();
        testList.add(test1);
        testList.add(test2);
        table.createMany(testList);
        List<TestEntity> insertedTests = new ArrayList<>();
        assertEquals(table.findAny(TestEntity.class, "firstName", name).size(), 2);
    }

    @Test
    void insert_notNullFieldWithNullData_expectException(){
        TestEntity test = new TestEntity();
        assertThrows(IllegalArgumentException.class, () -> table.createOne(test));
    }

    @Test
    void delete_removeOneUserFromDB_ExpectTrue() {
        TestEntity test = TestEntity.createRandomTest();
        table.createOne(test);
        assertEquals(1, table.deleteOne(TestEntity.class, "id", test.getId()));
    }

    @Test
    void delete_removeMultipleObjectsFromDB_ExpectTrue() {
        TestEntity test1 = TestEntity.createRandomTest();
        TestEntity test2 = TestEntity.createRandomTest();
        test1.setFirstName("yossi");
        test2.setFirstName("yossi");
        List<TestEntity> testList = new ArrayList<>();
        testList.add(test1);
        testList.add(test2);
        table.createMany(testList);
        assertEquals(2, table.deleteAny(TestEntity.class, "firstName", test1.getFirstName()));
    }

    @Test
    void delete_removeMultipleObjectsFromDBWhenTableIsEmpty_ExpectFalse() {
        assertEquals(0, table.deleteOne(TestEntity.class, "id", 1));
    }

    @Test
    void update_changeOneProperty_expectEquals() {
        TestEntity test = TestEntity.createRandomTest();
        test.setId(1);
        table.createOne(test);
        table.updateProperty(TestEntity.class, "id", 1, "firstName", "ziv");
        TestEntity newTest = table.findOne(TestEntity.class, "id", 1).get(0);
        assertEquals(newTest.getId(), test.getId());
    }

    @Test
    void update_changeEntireObject_expectEquals() {
        TestEntity test1 = TestEntity.createRandomTest();
        test1.setId(5);
        table.createOne(test1);
        String first = "yofi";
        test1.setFirstName(first);
        String last = "haha";
        test1.setLastName(last);
        table.updateEntireEntity(test1);
        TestEntity test2 = table.findOne(TestEntity.class, "id", 5).get(0);
        assertEquals(test2.getFirstName() == first, test2.getLastName() == last);
    }
}
