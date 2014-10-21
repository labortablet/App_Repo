package company;

import java.util.List;

import imports.LocalEntry;
import imports.Experiment;

/**
 * Created by Grit on 24.07.2014.
 */
public class ExperimentEntry {
    private Experiment experiments;
    private List<LocalEntry> entriesList;
    public ExperimentEntry(Experiment experiment, List<LocalEntry> entries)

    {
        experiments = experiment;
        entriesList = entries;
    }



    public Experiment getExperiments() {
        return experiments;
    }

    public List<LocalEntry> getEntriesList() {
        return entriesList;
    }

}
