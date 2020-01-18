package strata.app;

import strata.sequences.SegmentedSequenceFactory;
import strata.sequences.iface.Sequence;
import strata.sequences.iface.SequenceFactory;

public class App {
    public static void main(String[] args) {
        SequenceFactory sequenceFactory = SegmentedSequenceFactory.instance();
        Sequence aSequence = sequenceFactory.createSequence();

        for(int i = 0; i < 90; i++) {
            aSequence.append(i);
        }

        for(int i = 8; i >= 0; i--) {
            System.out.println("Cutting: From - " + i*10 + " To - " + ((i*10)+9));
            aSequence.cut(i*10, (i*10)+9);
        }
        aSequence.fileOut();
    }
}
