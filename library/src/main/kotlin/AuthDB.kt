import il.ac.technion.cs.softwaredesign.storage.impl.SecureStorageFactoryImpl

class AuthDB : DataBase(SecureStorageFactoryImpl(), "Auth".toByteArray())