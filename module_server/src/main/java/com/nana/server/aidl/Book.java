package com.nana.server.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Description:
 *
 * @author yangnana
 * @version 1.3.2
 * @since 1.3.2
 */
public class Book implements Parcelable {
    public int id;
    public String name;

    public Book() {

    }

    public Book(int id, String name) {
        this.id = id;
        this.name = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public Book(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    @Override
    public String toString() {
        return "id=" + id + ",name=" + name;
    }
}
