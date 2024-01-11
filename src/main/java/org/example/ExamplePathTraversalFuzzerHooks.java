package org.example;

import com.code_intelligence.jazzer.api.FuzzerSecurityIssueHigh;
import com.code_intelligence.jazzer.api.HookType;
import com.code_intelligence.jazzer.api.Jazzer;
import com.code_intelligence.jazzer.api.MethodHook;

import java.io.Reader;
import java.lang.invoke.MethodHandle;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExamplePathTraversalFuzzerHooks {

    @MethodHook(
            type = HookType.BEFORE,
            targetClassName = "java.io.FileReader",
            targetMethod = "<init>",
            targetMethodDescriptor = "(Ljava/lang/String;)V")
    public static void hookFileReader(MethodHandle handle, Object thisObject, Object[] args, int hookId) {

        String fileToRead = (String) args[0];

        Path targetFile;
        Path legitPath;

        try {
            targetFile = Paths.get(fileToRead).normalize();
            legitPath = FuzzLFI.uploadDir.normalize();
        } catch (InvalidPathException ignored) {
            return;
        }

        // Check the upload path
        if (!targetFile.startsWith(legitPath)) {
            // If LFI detected, report it
            Jazzer.reportFindingFromHook(
                    new FuzzerSecurityIssueHigh("Tried to read " + targetFile + " which is not in a legitimate path:" + legitPath)
            );
        }

    }
}