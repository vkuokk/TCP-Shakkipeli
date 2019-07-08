# TCP-Shakkipeli

## Getting Started

You will need both JavaFX and Java versions 12 or newer, older versions not tested.
JavaFX: https://gluonhq.com/products/javafx/

### Prerequisites
Instructions for setting environment variables for JavaFX can be found here: https://openjfx.io/openjfx-docs/#install-javafx

Compiling (windows)
```
javac --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml Shakkipeli/Main.java
```
(linux)
```
javac --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml Shakkipeli/Main.java
```


Running (windows)
```
java --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml Shakkipeli.Main
```
(linux)
```
java --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml Shakkipeli.Main.
```



## Built With

* Java JDK 12.0.1
* JavaFX SDK 12.0.1

## Authors

**Ville Kuokkanen** 2019

## License

This project is licensed under the MIT License. LICENSE.txt for details.
