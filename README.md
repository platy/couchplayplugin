Time counter example for CouchDB play plugin
============================================

^The play framework plugin hasn't yet been extracted from this application - if you have the time and inclintation to do it that would be appreciated^

What this does
--------------

1. Provides a scala api for communicating with a couchdb server, built on play framework's async WS api



Future things
-------------

1. Extract plugin for play framework
2. Add a startup hook to check that the design docs on the couch server match those in the conf directory
3. Make uploading updated design documents easy
4. Test against various versions to find the compatibility bounds
5. Set up automated testing

Compatibility
-------------

Tested with couchdb 1.2.0, the parts of the couch api used by this library are very common and so are unlikely to change very much. I would assume that this works with many versions of couchdb.

Tests are provided which test against the local database and should bring up any compatibility problems.
