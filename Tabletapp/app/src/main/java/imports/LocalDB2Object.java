//First version for a interface between the GUI and the LocalDB.
//the GUI should only deal with well defined Obejcts while
//This object takes care of all the DB stuff

package imports;

import java.util.LinkedList;

import exceptions.NoInternetAvailable;

public class LocalDB2Object {

    User register_user(String login, String password){
        return null;
    };

    Project register_project(User user, String project_name){
        return null;
    };

    Experiment register_experiment(User user, Project project, String experiment_name){
        return null;
    };

    //FIXME Local Entry??? We should either call them all Local or all without a prefix
    LocalEntry new_Entry(User user, Experiment experiment, String title, AttachmentBase attachment){
        return null;
    };

    LinkedList<Project> match_project(Project project){
        return null;
    };

    LinkedList<Project> match_project(Project project, Project real_project){
        return null;
    };

    LinkedList<Project> match_project(Project project, boolean force_sync) throws NoInternetAvailable{
        return null;
    };

    LinkedList<Project> match_project(Project project, Project real_project, boolean force_sync) throws NoInternetAvailable{
        return null;
    };

    LinkedList<Experiment> match_experiment(Experiment experiment){
        return null;
    };

    LinkedList<Experiment> match_experiment(Experiment experiment, Experiment real_experiment){
        return null;
    };

    LinkedList<Experiment> match_experiment(Experiment experiment, boolean force_sync) throws NoInternetAvailable{
        return null;
    };

    LinkedList<Experiment> match_experiment(Experiment experiment, Experiment real_experiment, boolean force_sync) throws NoInternetAvailable{
        return null;
    };

    Boolean check_user(User user) throws NoInternetAvailable{
        return null;
    };

    LinkedList<Project> get_projects(User user){
        return null;
    };

    LinkedList<Project> get_projects(User user, boolean force_sync) throws NoInternetAvailable{
        return null;
    };

    LinkedList<Experiment> get_experiments(User user, Project project){
        return null;
    };

    LinkedList<Experiment> get_experiments(User user, Project project, boolean force_sync) throws NoInternetAvailable{
        return null;
    };

    LinkedList<LocalEntry> get_entries(User user, Experiment experiment, int number){
        return null;
    };

    LinkedList<LocalEntry> get_entries(User user, Experiment experiment, int number, boolean force_sync) throws NoInternetAvailable{
        return null;
    };

}
