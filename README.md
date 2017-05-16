# generals-io-trainer



To run bot:

clone repository
1.  execute ./gradlew build
2.  execute ./gradlew run

Expects the replay zip file location here:
`/trainingdata/generals.io replays.zip`

* Defaults to saving a single file per replay.
* Currently only implemented to save the map data prior to move.
* Map data is to be used as a 30 x 30 x 7  matrix for use in a convolutional NN

Enjoy.
