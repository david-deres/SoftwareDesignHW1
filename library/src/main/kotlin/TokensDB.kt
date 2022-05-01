import il.ac.technion.cs.softwaredesign.storage.impl.SecureStorageFactoryImpl

class TokensDB : DataBase(SecureStorageFactoryImpl(), "Tokens".toByteArray())