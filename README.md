#WJDBC
==
Fork of [**VJDBC**](http://vjdbc.sourceforge.net/) project.

##Motivation
The motivation of this project is to increase the overall throughput of vjdbc in LAN environments.

##Changes
- Only RMI communication method supported.
- Data compression at SocketFactory level, see [CompressiongOutputStream](http://stackoverflow.com/questions/2374374/java-rmi-ssl-compression-impossible)
- Compression method changed to LZF, see [compress](https://github.com/ning/compress).
- Upgrade compatibility from JSE1.4 to JSE1.6.
- Client-side dependency on commons-logging eliminated.
- XML configuration file format changed.
