package com.pandaproductions.beefit.Database

import androidx.room.TypeConverter
import com.pandaproductions.beefit.ENUM.FairyBreadType
import com.pandaproductions.beefit.ENUM.PetType

class Converters {
    companion object {
        @TypeConverter
        @JvmStatic
        fun getPetType(int: Int): PetType {
            for (type in PetType.values()) {
                if (type.num == int) {
                    return type
                }
            }
            return PetType.DEER // default
        }

        @TypeConverter
        @JvmStatic
        fun getPetTypeInt(type: PetType): Int {
            if (type != null) {
                return type.num
            }
            return PetType.DEER.num //default
        }

        @TypeConverter
        @JvmStatic
        fun getFairyBreadType(int: Int): FairyBreadType {
            for (type in FairyBreadType.values()) {
                if (type.num == int) {
                    return type
                }
            }
            return FairyBreadType.PASTEL // default
        }

        @TypeConverter
        @JvmStatic
        fun getFairyBreadTypeInt(type: FairyBreadType): Int {
            if (type != null) {
                return type.num
            }
            return FairyBreadType.BRIGHT.num //default
        }
    }
}