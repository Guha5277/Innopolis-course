package task01.compiler;

import task01.model.Worker;

import javax.tools.*;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;

public class CodeCompiler {
    private final String BYTECODE_DIR;
    private final String BYTECODE_FILE_NAME = "SomeClass.class";
    private SourceMaker sourceMaker;

    public CodeCompiler() {
        sourceMaker = new SourceMaker();
        BYTECODE_DIR = sourceMaker.getSourceDir();
    }

    public void compileAndRun(String code) {
        sourceMaker.makeSource(code);
        compileToByteCode(sourceMaker.getSourceDest());
        loadAndRunClass(BYTECODE_DIR + BYTECODE_FILE_NAME);
    }

    private void loadAndRunClass(String bytecodeDest) {
        Class<?> aClass = loadClass(bytecodeDest);
        Constructor<?> constructor = aClass.getConstructors()[0];
        try {
            Worker worker = (Worker)constructor.newInstance();
            worker.doWork();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void compileToByteCode(String sourceDest) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

        Iterable<? extends JavaFileObject> compilationUnit
                = fileManager.getJavaFileObjectsFromFiles(Collections.singletonList(new File(sourceDest)));

        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnit);
        boolean success = task.call();

        showCompilerMessages(diagnostics, success);

    }

    private void showCompilerMessages(DiagnosticCollector<JavaFileObject> diagnostics, boolean compilerResult) {
        for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
            System.out.println(diagnostic.getCode());
            System.out.println(diagnostic.getKind());
            System.out.println(diagnostic.getPosition());
            System.out.println(diagnostic.getStartPosition());
            System.out.println(diagnostic.getEndPosition());
            System.out.println(diagnostic.getSource());
            System.out.println(diagnostic.getMessage(null));
        }
        System.out.println("Compile source code success: " + compilerResult);

    }

    private Class<?> loadClass(String classPath) {
        ClassLoader classLoader = new ClassLoader(java.lang.ClassLoader.getSystemClassLoader());
        try {
            return classLoader.loadMyClass(classPath);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load class: " + classPath);
        }
    }
}
