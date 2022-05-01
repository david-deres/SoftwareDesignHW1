import il.ac.technion.cs.softwaredesign.storage.impl.SecureStorageFactoryImpl

class UsersDB : DataBase(SecureStorageFactoryImpl(), "Users".toByteArray())