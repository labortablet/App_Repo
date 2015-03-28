package company;

import java.util.List;

import datastructures.Entry;
import datastructures.Experiment;

/**
 * Created by Grit on 24.07.2014.
 */
public class ExperimentEntry {
    private Experiment experiments;
    private List<Entry> entriesList;
    public ExperimentEntry(Experiment experiment, List<Entry> entries)

    {
        experiments = experiment;
        entriesList = entries;
    }



    public Experiment getExperiments() {
        return experiments;
    }

    public List<Entry> getEntriesList() {
        return entriesList;
    }
    public int getEntryIDByCreationTimestamp(Long timestamp)
    {int entry_id = 0;
        for(int i = 0; i < entriesList.size();i++)
        {
            if (entriesList.get(i).getEntry_time().equals(timestamp))
            {  entry_id = i;
            }
        }
        return entry_id;
    }

}
