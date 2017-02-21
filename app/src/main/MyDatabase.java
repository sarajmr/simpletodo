package com.sarajmudigonda.mysimpletodo;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Database(name = MyDatabase.NAME, version = MyDatabase.VERSION)
public class MyDatabase {
    public static final String NAME = "MyDataBase";

    public static final int VERSION = 1;
}

// **Note:** Your class must extend from BaseModel
// Make sure that your table names are upper camel case (Organization, OrganizationTable, etc.) for DBFlow3
@Table(database = MyDatabase.class)
public class Organization extends BaseModel {
    // ... field definitions that map to columns go here ...
}

// **Note:** Your class must extend from BaseModel
@Table(database = MyDatabase.class)
public class Organization extends BaseModel {

    @Column
    @PrimaryKey
    int id;

    @Column
    String name;
}

@Table(database = MyDatabase.class)
public class User extends BaseModel {
    @Column
    @PrimaryKey
    int id;

    @Column
    String name;

    @Column
    @ForeignKey(saveForeignKeyModel = false)
    Organization organization;

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public void setName(String name) {
        this.name = name;
    }
}

