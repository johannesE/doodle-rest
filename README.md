doodle-rest
===========

Doodle-like REST API backed by Cassandra
========================

For the exercises:

2: Simple data manipulation

[default@unknown] CREATE KEYSPACE K1
WITH placement_strategy = 'org.apache.cassandra.locator.SimpleStrategy'
AND strategy_options = {replication_factor:1};

[default@unknown] use K1;

[default@K1] CREATE COLUMN FAMILY CF1
WITH comparator = AsciiType
AND key_validation_class=AsciiType
AND default_validation_class = UTF8Type;

[default@K1] SET CF1['k1']['cn1']='v1';

[default@K1] SET CF1['k1']['cn2']='é';

[default@K1] SET CF1['k1']['cn3é']='v3';

[default@K1] GET CF1[utf8('k1')][utf8('cn1')]; (returns v1)


3: Cluster maintenance:

Listing the node(s): New terminal in the bin folder:
nodetool ring (returns: 

==========
Datacenter: datacenter1

Replicas: 1

Address    Rack        Status State   Load            Owns                Token                                       

127.0.0.1  rack1       Up     Normal  55.7 KB         100.00%             1234567890     )       

To change the initial token we set "initial_token: 1234567890" in the cassandra.yaml

Then again with the nodetool: "nodetool compact K1"

"nodetool compact"

"describe cluster" returns the schema version


Project / Service
==================


In order to run the project, you have to set up a tomcat server on port 8080. Otherwise the tests won't pass.


Database SetUp
==============

[default@unknown] CREATE KEYSPACE doodle
WITH placement_strategy = 'org.apache.cassandra.locator.SimpleStrategy'
AND strategy_options = {replication_factor:1};

[default@unknown] use doodle;

[default@doodle] CREATE COLUMN FAMILY stats
WITH comparator = AsciiType
AND key_validation_class=AsciiType
AND default_validation_class = UTF8Type;

[default@doodle] CREATE COLUMN FAMILY outgoers
WITH comparator = AsciiType
AND key_validation_class=AsciiType
AND default_validation_class = UTF8Type;

With these commands the Database we use is created.
