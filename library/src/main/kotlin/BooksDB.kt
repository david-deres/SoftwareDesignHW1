import il.ac.technion.cs.softwaredesign.storage.impl.SecureStorageFactoryImpl

class BooksDB : DataBase(SecureStorageFactoryImpl(), "Books".toByteArray())