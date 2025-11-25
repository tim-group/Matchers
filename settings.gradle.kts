pluginManagement {
    val repoUrl = providers.gradleProperty("repoUrl")
    val codeartifactUrl = providers.environmentVariable("CODEARTIFACT_URL")
        .orElse(providers.gradleProperty("codeartifact.url"))
        .orElse("https://timgroup-148217964156.d.codeartifact.eu-west-1.amazonaws.com/maven/jars/")
    val codeartifactToken = providers.environmentVariable("CODEARTIFACT_TOKEN")
        .orElse(providers.gradleProperty("codeartifact.token"))
    repositories {
        gradlePluginPortal()
        if (repoUrl.isPresent) {
            maven(url = "${repoUrl.get()}/groups/public") {
                name = "nexus"
                isAllowInsecureProtocol = true
            }
        }
        if (codeartifactUrl.isPresent && codeartifactToken.isPresent) {
            maven(url = codeartifactUrl.get()) {
                name = "codeartifact"
                credentials {
                    username = "aws"
                    password = codeartifactToken.get()
                }
            }
        }
    }
}

rootProject.name = "Matchers"

include("http-client")
