package mx.edu.utng.sitiomovies

import android.os.Parcel
import android.os.Parcelable

data class Movie(
    val id: Int,
    val title: String,
    val genre: String,
    val year: Int,
    val director: String,
    val duration: Int,
    val rating: Float,
    val description: String,
    val image: String // Agregado el campo image
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readFloat(),
        parcel.readString() ?: "",
        parcel.readString() ?: "" // Añadido para leer el campo image
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(genre)
        parcel.writeInt(year)
        parcel.writeString(director)
        parcel.writeInt(duration)
        parcel.writeFloat(rating)
        parcel.writeString(description)
        parcel.writeString(image) // Añadido para escribir el campo image
    }

    override fun describeContents(): Int = 0

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<Movie> {
            override fun createFromParcel(parcel: Parcel): Movie {
                return Movie(parcel)
            }

            override fun newArray(size: Int): Array<Movie?> {
                return arrayOfNulls(size)
            }
        }
    }
}
