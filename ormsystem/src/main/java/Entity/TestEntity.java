package Entity;

import Annotation.NotNull;
import Annotation.PrimaryKey;
import Annotation.Unique;
import Utils.RandomData;

public class TestEntity {

    @Unique
    private int id;
    @NotNull
    private String firstName;
    private String lastName;

    public TestEntity() {

    }
    public static TestEntity createRandomTest() {
        TestEntity test = new TestEntity();
        test.setFirstName(RandomData.getRandomFirstName());
        test.setLastName(RandomData.getRandomLastName());
        test.setId(RandomData.getRandomId());
        return test;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
