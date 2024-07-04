# Config in Partly Sane Skies

There are two main config types in Partly Sane Skies: OneConfig, Partly Sane Config (PSConfig/PSConf). Raw JSON files, can also be used, but should primarirly be used for storing data when OneConfig or PSConfig cannot be used.

## OneConfig

OneConfig is current our most end-user friendly config library. 

The OneConfig class is located at ``me.partlysanestudios.partlysaneskies.config.OneConfigScreen`` and is written entirely in Kotlin.

Within the class, we seperate each new category using:

```kt

// ------------- Category: CATEGORY_NAME ---------------------------------

```

and we seperate each new subcategory using:

```kt

// SUBCATEGORY_NAME

```

To add a config option, create a new ``var`` for your config option, and assign it a default value. You can then use OneConfig annotations to define it's type. 

Example ``Switch`` variable:
```kt
@Switch(
    name = "Example Name",
    description = "Example Description",
    category = "Example Category",
    subcategory = "Example Subcategory"
)
var exampleVarDefinition = false
```

The annotation parameters should be listed in the following order:

1. Name
2. Description
3. Category
4. Subcategory
5. Size (not required)
6. Depending on the annotation, the rest of the parameters are different.


Due to a bug in OneConfig, all config values that involve strings must be added to the ``resetBrokenStringsTick()`` function, with the following format:

```kt
if (exampleVar.isEmpty()) {
    exampleVar = "Default Example Value!"
    save()
}
```

## Partly Sane Config

Partly Sane Config (PSConfig/PSConf) is a propriatary config library aimed at by dynamic, as opposed to using variable annotations (like OneConfig). It is also designed to be highly modular. 

### Use Instructions

To create a new config, initialize a new ``Config`` object and register it with the ``ConfigManager`` object.

Example:

```kt
private val config = Config()

init {
    ConfigManager.registerConfig("path/to/save.json", config)
}
```

*Configs should be created as class/object variables*

To add options, you can use the method ``.registerOption()`` on the instance of the config object. 

Example creating a config, registering a Toggle type with the default value ``true``, and registering the config with ``ConfigManager``:

```kt
private val config = Config()
    .registerOption("exampleConfigKey", Toggler(name = "Example Toggle Display Name", description = "Example toggle description", defaultValue = true))

init {
    ConfigManager.registerConfig("path/to/save.json", config)
}
```



To retrive an option, you can use the ``find()`` method on the config object.

Example retriving the ``Toggle`` option created in the previous example:

```kt
config.find("exampleConfigKey").asBoolean
```
*Certain ``ConfigOption`` types may have extension functions that allow you to quickly access the state of the config*

Using the find method, you can also modify the state of a config option

Example modifying the ``Toggle`` option from the previous example:
```kt
config.find("exampleConfigKey").asToggle.state = false
```

### Creating New Config Option Type

To create a new config option type, create a new class in the ``me.partlysanestudios.config.psconfig`` package, that inherits the type ``ConfigOption`` (located in the same package)

The class must implement the ``saveToJson`` method, which must return a ``JsonElement`` containing the save state of the option.

The class must implement the ``loadFromJson`` method, which is passed a JsonElement (in the same format as the ``saveToJson`` method returns) and must load the previous save state.

See ``Toggle`` class for example.
