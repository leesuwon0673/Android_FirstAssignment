package kr.co.lion.android_firstassignment

import android.os.Parcel
import android.os.Parcelable

class SaveData(var title: String?, var contents:String?, var nowDate:String?): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(contents)
        parcel.writeString(nowDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SaveData> {
        override fun createFromParcel(parcel: Parcel): SaveData {
            return SaveData(parcel)
        }

        override fun newArray(size: Int): Array<SaveData?> {
            return arrayOfNulls(size)
        }
    }
}