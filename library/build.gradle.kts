val externalLibraryVersion: String? by extra
val guiceVersion: String? by extra


dependencies {
    implementation("il.ac.technion.cs.softwaredesign", "primitive-storage-layer", externalLibraryVersion)
    implementation("com.google.inject", "guice", guiceVersion)
}