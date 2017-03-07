# Imports the monkeyrunner modules used by this program
from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice

# Connects to the current device, returning a MonkeyDevice object
device = MonkeyRunner.waitForConnection()

# Installs the Android package. Notice that this method returns a boolean, so you can test
# to see if the installation worked.
#device.installPackage('myproject/bin/MyApplication.apk')

# sets a variable with the package's internal name
package = 'com.app.fbulou.commandexample'

# sets a variable with the name of an Activity in the package
activity = 'com.app.fbulou.commandexample.MainActivity'

# sets the name of the component to start
runComponent = package + '/' + activity

# Runs the component
device.startActivity(component=runComponent)

# Presses the Menu button
#device.press('KEYCODE_COMMA', MonkeyDevice.DOWN_AND_UP)

device.wake()

# Adding a delay, so we can get a better screenshot
MonkeyRunner.sleep(2)

# Takes a screenshot
#result = device.takeSnapshot()

# Writes the screenshot to a file
#result.writeToFile('/home/fab/Pictures/shot1.png','png')

