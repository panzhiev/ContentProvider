package com.tim.contentprovider.model;

import java.io.Serializable;

/**
 * Created by Tim on 25.01.2017.
 */

public class Person implements Serializable {
    private int mId;
    private String mName;
    private String mSurname;
    private String mPhone;
    private String mMail;
    private String mSkype;
    private String mProfile;

    public static Person selectedPerson;

    public Person() {
        super();
    }

    public Person(String mMail, String mName, String mPhone, String mSkype, String mSurname) {
        this.mMail = mMail;
        this.mName = mName;
        this.mPhone = mPhone;
        this.mSkype = mSkype;
        this.mSurname = mSurname;
    }

    public Person(int mId, String mName, String mSurname, String mPhone, String mMail, String mSkype, String mProfile) {
        this.mId = mId;
        this.mName = mName;
        this.mSurname = mSurname;
        this.mPhone = mPhone;
        this.mMail = mMail;
        this.mSkype = mSkype;
        this.mProfile = mProfile;
    }

    public String getmProfile() {
        return mProfile;
    }

    public void setmProfile(String mProfile) {
        this.mProfile = mProfile;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmMail() {
        return mMail;
    }

    public void setmMail(String mMail) {
        this.mMail = mMail;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public String getmSkype() {
        return mSkype;
    }

    public void setmSkype(String mSkype) {
        this.mSkype = mSkype;
    }

    public String getmSurname() {
        return mSurname;
    }

    public void setmSurname(String mSurname) {
        this.mSurname = mSurname;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime + result + mId;
        return  result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return  true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Person other = (Person) obj;

        if (mId != other.mId)
            return false;
        return true;
    }
}
