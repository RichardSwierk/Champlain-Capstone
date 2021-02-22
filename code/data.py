import sys
import usb.core

vid=0x10b6
pid=0x0502
#vid=0x0bda
#pid=0x8179
configuration=1
interface=0
setting=0
endpoint=1

dev = usb.core.find(idVendor=vid, idProduct=pid)
if dev is None:
        print("Device not found")
        sys.exit(1)
if dev.is_kernel_driver_active(interface):
        print("Detaching kernel driver")
        dev.detach_kernel_driver(interface)
config=dev.get_active_configuration()
interface=config[(interface, setting)]
endpoint=interface[endpoint]
command='/x1b/x1c/x2a/x2e/xc5/x8f/xc3/x1d/x1f/x8e/xe0/x00/x00/x00/x00'

#command='/x60/x0f/xed/xfb/x00/x28/x06/x40/xfe/x80/x00/x00/x00/x00/x00/x00/x5f/x44/x67/xcf/xee/xc8/xe6/x61/x26/x00/x19/x01/x00/x00/x38/xd7/x00/x00/x00/x00/x00/x00/x00/x00'

endpoint.write(command)
