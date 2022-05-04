package il.ac.technion.cs.softwaredesign

import dev.misfitlabs.kotlinguice4.KotlinModule
import il.ac.technion.cs.softwaredesign.storage.SecureStorageFactory



class SecureStorageModule : KotlinModule() {

    override fun configure() {
        bind<SecureStorageFactory>().to<TestSecureStorageFactory>()
    }
}