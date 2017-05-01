package trumeet.IfwManager.workspace;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ShellUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import trumeet.IfwManager.server.firewall.IntentFirewall;

/**
 * Created by Trumeet on 2017/3/31.
 * A IFW manage workspace.
 * @author Trumeet
 * @since 1.0
 */

public class Workspace {
    private static final String TAG = "Workspace";
    public static final String ID_CURRENT = "current";

    private static Workspace current;

    public static Workspace getCurrent () {
        return current;
    }

    public static void setCurrent (Workspace workspace) {
        current = workspace;
    }

    private String id;
    private File workspaceDir;
    private File ruleDir;
    private boolean modified;

    public Workspace (String id, Context context) {
        this.id = id;
        // TODO: Very dangerous take workspace in sdcard.
        workspaceDir = new File(context.getExternalCacheDir().
                getAbsolutePath() + "/workspace/" + id);
    }

    public void init () throws IOException {
        final String TAG = "init";
        LogUtils.i(TAG, "init workspace. ", id);
        // Step 1, Check and delete exists workspace
        if (workspaceDir.exists()) {
            LogUtils.i(TAG, "Workspace exists, deleting");
            if (!workspaceDir.delete()) {
                LogUtils.w(TAG, "Delete using root");
                ShellUtils.CommandResult result =
                        ShellUtils.execCmd("rm -rf " + workspaceDir.getAbsolutePath()
                                , true, true);
                if (result.result != 0) {
                    LogUtils.e(TAG, "delete exists workspace", result.successMsg,
                            result.errorMsg, result.result);
                    throw new IOException("Unable to delete exists workspace");
                } else {
                    LogUtils.i(TAG, "Delete successful use root");
                }
            } else {
                LogUtils.i(TAG, "Delete successful use file.delete()");
            }
        }
        // Step 2, Make workspace dir
        LogUtils.i(TAG, "Make workspace dir");
        if (!workspaceDir.mkdirs()) {
            LogUtils.e(TAG, "Unable to make workspace dir");
            throw new IOException("Unable to make workspace dir");
        } else {
            LogUtils.i(TAG, "Make workspace dir successful");
        }
        // Step 3, Make rule dir
        ruleDir = new File(workspaceDir.getAbsolutePath() + "/rule/");
        if (!ruleDir.mkdir()) {
            LogUtils.e(TAG, "Unable to make rule dir");
            throw new IOException("Unable to make rule dir");
        } else {
            LogUtils.i(TAG, "Make rule dir successful");
        }
        LogUtils.i(TAG, "Workspace init successful.");
        /*
        File test = new File(workspace.getRuleDir() + "/1a.txt");
                try {
                    if (!test.createNewFile()) throw new Exception("Return false");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
         */
    }

    public void fillBySystemProfile () {
        String command = "cp -R " + IntentFirewall.getRulesDir().getAbsolutePath() + "/*.*"
                + " " + ruleDir.getAbsolutePath();
        try {
            ShellUtils.CommandResult result = ShellUtils.execCmd(command, true, true);
            LogUtils.i(TAG, "copy system profile", result.successMsg,
                    result.errorMsg, result.result);
            if (result.result != 0) {
                throw new IOException("Return code error");
            }
            // Fix permissions.
            File[] files = getRuleDir().listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".xml");
                }
            });
            if (files == null) {
                return;
            }
            List<String> permissionCommands = new ArrayList<>();
            int uid = android.os.Process.myUid();
            for (final File file : files) {
                permissionCommands.add(
                        "chown -R " + uid + ":" + uid + " " + file.getAbsolutePath()
                );
                permissionCommands.add(
                        "chmod 600 " + file.getAbsolutePath()
                );
            }
            ShellUtils.CommandResult resultPermission = ShellUtils.execCmd(permissionCommands, true, true);
            LogUtils.i(TAG, "fix permission", resultPermission.successMsg,
                    resultPermission.errorMsg, resultPermission.result);
            if (resultPermission.result != 0) {
                throw new IOException("Return code error");
            }
            /*
            try {
                File file = new File(ruleDir.getAbsolutePath()
                        + "/gib.xml");
                LogUtils.i(TAG, "Test", file,
                        file.canRead(),
                        file.canWrite(),
                        file.canExecute());
                FileInputStream s = new FileInputStream(file);
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            */
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getId() {
        return id;
    }

    public File getWorkspaceDir() {
        return workspaceDir;
    }

    public File getRuleDir() {
        return ruleDir;
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }
}
