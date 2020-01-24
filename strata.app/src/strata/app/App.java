package strata.app;

import strata.sequences.iface.Sequence;
import strata.sequences.iface.SequenceFactory;

public class App {
    public static void main(String[] args) {
        SequenceFactory sequenceFactory = SequenceFactory.createFactory();
        Sequence aSequence = sequenceFactory.createSequence();

        for(int i = 0; i < 10; i++) {
            aSequence.append(i);
        }

        aSequence.fileOut();
    }
}
