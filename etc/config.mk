INSTALLDIR = src lib \
	src/edu src/edu/uwm src/edu/uwm/cs351

INSTALLSRC = \
    src/TestDirect.java \
	src/TestInvariant.java \
	src/TestCollection.java \
	src/TestEfficiency.java \
	src/TestLinkedCollection.java

INSTALLSKEL = \
	src/edu/uwm/cs351/LinkedCollection.java

INSTALLJAR = \
	${LOCKEDTESTS} ${RANDOMTESTS} \
	-C ../src edu/uwm/cs351/AddableIterator.java \
	          edu/uwm/cs351/AddableIterator.class
