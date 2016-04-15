# README #

Create a simple add-on package contaning plugins .jar to install from Nuxeo UI.

### How do I get set up? ###

* create your new addon
```shell
	./create.sh test-addon "My test addon" 1.0 cap-6.0 https://github.com/athento/test-addon "My very long description"
```

* Copy your plugin .jar files to plugin folder

* Create the package
```shell
./package.sh test-addon
```

* Install the resulting package (zip file located at dist directory) from Nuxeo UI (Admin, Update Center, Local Packages).
