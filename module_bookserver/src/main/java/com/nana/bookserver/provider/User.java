package com.nana.bookserver.provider;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Description:
 *
 * @author yangnana
 * @version 1.3.2
 * @since 1.3.2
 */
public class User implements Parcelable {
    public int userId;
    public String userName;
    public boolean isMale;

    public User() {

    }

    public User(int id, String name, boolean isMale) {
        this.userId = id;
        this.userName = name;
        this.isMale = isMale;
    }

    /**
     * 这里的读的顺序必须与writeToParcel(Parcel dest, int flags)方法中
     * 写的顺序一致，否则数据会有差错，比如你的读取顺序如果是：
     * nickname = source.readString();
     * username=source.readString();
     * age = source.readInt();
     * 即调换了username和nickname的读取顺序，那么你会发现你拿到的username是nickname的数据，
     * 而你拿到的nickname是username的数据
     *
     * @param in
     */
    protected User(Parcel in) {
        userId = in.readInt();
        userName = in.readString();
        isMale = in.readByte() != 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeString(userName);

        //if isMale == true, byte == 1
        dest.writeByte((byte) (isMale ? 1 : 0));
    }
}
