package task01.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import task01.core.GameField;
import task01.core.GameListener;
import task01.core.TheLifeGame;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

public class App extends Application implements GameListener, CanvasEventsListener {
    private final static int MIN_WIDTH = 550;
    private final static int MIN_HEIGHT = 300;
    private final static String XML_PATH = "/javafxmls/main_view.fxml";
    private final static int BENCH_FIRST_RESULT_INDEX = 2;
    private final static int BENCH_FIRST_PROGRESSBAR_INDEX = 3;

    private boolean isGameRunning;
    private int benchElementsIndex = 1;
    private int currentBenchItemIndex = 1;
    private int currentTotalGens;

    private Mode currentMode;
    private Stage mainStage;
    private TheLifeGame game;
    private GameField previewGameField;
    private ResizableCanvas gameCanvas;
    private File lastOpenedDirectory;
    private Stage calculateStage;
    private ProgressBar calculateProgressBar;

    private VBox scrollContainer;
    private Map<Integer, BenchmarkItem> benchmarkItemMap = new HashMap<>();
    private GameField benchFirstGameField;
    private GameField benchSecondGameField;
    private BenchmarkItem currentItem;
    private ProgressBar currentBenchProgressBar;
    private Label currentTimeLabel;

    //Tabs
    @FXML
    private Tab tabGame;
    @FXML
    private Tab tabBenchmark;

    //Game Tab
    @FXML
    private AnchorPane pane;
    @FXML
    private TextField fieldX;
    @FXML
    private TextField fieldY;
    @FXML
    private TextField fieldFillPercentage;
    @FXML
    private Button btnPreview;
    @FXML
    private TextField fieldGens;
    @FXML
    private TextField fieldThreads;
    @FXML
    private TextField fieldFrameTime;
    @FXML
    private Button btnStart;
    @FXML
    private Button btnStop;
    @FXML
    private Button btnSaveToFile;
    @FXML
    private Button btnLoadFromFIle;
    @FXML
    private Label lblInfo;

    //Benchmark Tab
    @FXML
    private ScrollPane benchScrollPane;
    @FXML
    private Button btnAddToBench;
    @FXML
    private Button btnStartBench;
    @FXML
    private Button btnStopBench;
    //First to compare field
    @FXML
    private TextField fieldFirstXBench;
    @FXML
    private TextField fieldFirstYBench;
    @FXML
    private TextField fieldFirstFillBench;
    @FXML
    private TextField fieldFirstGensBench;
    @FXML
    private TextField fieldFirstThreadsBench;
    @FXML
    private Button btnFirstLoadFromFileBench;
    //Second to compare Field
    @FXML
    private TextField fieldSecondXBench;
    @FXML
    private TextField fieldSecondYBench;
    @FXML
    private TextField fieldSecondFillBench;
    @FXML
    private TextField fieldSecondGensBench;
    @FXML
    private TextField fieldSecondThreadsBench;
    @FXML
    private Button btnSecondLoadFromFileBench;
    @FXML
    private CheckBox checkSecondSameFieldsBench;
    @FXML
    private CheckBox checkLoadFileSecondBench;
    @FXML
    private CheckBox checkLoadFileFirstBench;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(XML_PATH));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.show();
        mainStage = primaryStage;
    }

    @FXML
    private void initialize() {
        initTabs();
        initGameInstance();
        initFields();
        initCheckBoxes();
        initBenchmarkScrollContainer();
        currentMode = Mode.GAME;
    }

    //Game Events
    @Override
    public void onGameStarted(int threadsCount) {
        Platform.runLater(() -> {
            if (currentMode == Mode.GAME) {
                isGameRunning = true;
                lockUIComponents();
            } else {
                currentItem = benchmarkItemMap.get(currentBenchItemIndex);
                //first from node
                benchGameStarted(currentItem.getFieldFirstEnd() == null);
            }
        });
    }

    @Override
    public void onGenChanged(GameField gameField, int currentGen) {
        Platform.runLater(() -> {
            if (currentMode == Mode.GAME) {
                calculateProgressBar.setProgress((double) currentGen / currentTotalGens);
                gameCanvas.addToQueue(gameField);
            } else {
                currentTimeLabel.setText("Поколение: " + currentGen);
                currentBenchProgressBar.setProgress((double) currentGen / currentTotalGens);
            }
        });
    }

    @Override
    public void fieldStateChanged(boolean state, int x, int y) {

    }

    @Override
    public void gameFinished(GameField gameField) {
        Platform.runLater(() -> {
            if (currentMode == Mode.GAME) {
                calculateStage.close();
                previewGameField = gameField;
                gameCanvas.addToQueue(gameField);
                gameCanvas.startPainting();
            } else {
                if (currentItem.getFieldFirstEnd() == null) {
                    currentItem.setFieldFirstEnd(gameField);
                } else {
                    currentItem.setFieldSecondEnd(gameField);
                }
                //TODO добавить верификацию результатов
            }
        });
    }

    @Override
    public void gameFinishedTime(GameField gameField, Long time) {
        Platform.runLater(() -> {
            Duration duration = Duration.of(time, ChronoUnit.MILLIS);
            if (currentMode == Mode.GAME) {
                lblInfo.setText("Игра окончена! Прожито поколений: " + gameField.getGen() + ". Затраченное время: "
                        + time + " || " + duration.toMinutes() + " минут(а), "
                        + duration.getSeconds() + " секунд");
            } else {
                currentTimeLabel.setText(time > 1000 ? duration.getSeconds() + " сек" : time + " мс");
                currentBenchProgressBar.setDisable(true);
                searchAndRunNextGame();
            }
        });
    }

    @Override
    public void successfulLoadFromFile(File file, GameField gameField) {
        lblInfo.setText("Успешно загружено из файла: " + file);
        fieldX.setText(String.valueOf(gameField.getX()));
        fieldY.setText(String.valueOf(gameField.getY()));
        fieldFillPercentage.setText(String.valueOf(gameField.getFillPercentage()));
        previewGameField = gameField;
        gameCanvas.preview(gameField);
    }

    @Override
    public void failedToReadFile(File file, IOException e) {
        makeDialogWindow(Alert.AlertType.ERROR,
                "Ошибка чтения файла!",
                String.valueOf(e.getCause()),
                e.getMessage()).showAndWait();
    }

    @Override
    public void wrongFileFormat(File file, RuntimeException e) {
        makeDialogWindow(Alert.AlertType.ERROR,
                "Неверный формат файла!",
                String.valueOf(e.getCause()),
                e.getMessage()).showAndWait();
    }

    @Override
    public void successfulSaveToFile(GameField gameField, File file) {
        lblInfo.setText("Успешно сохранено в файл! " + file);
    }

    @Override
    public void failedToSaveToFile(GameField gameField, File file, IOException e) {
        makeDialogWindow(Alert.AlertType.ERROR,
                "Ошибка записи файла!",
                String.valueOf(e.getCause()),
                e.getMessage()).showAndWait();
    }

    @Override
    public void onGameException(Throwable e) {
        makeDialogWindow(Alert.AlertType.ERROR,
                "Ошибка!",
                String.valueOf(e.getCause()),
                e.getMessage()).showAndWait();
    }

    //Canvas event
    @Override
    public void onAnimationEnd() {
        if (isGameRunning) {
            isGameRunning = false;
            unlockUIComponents();
        }
    }

    //Events UI listeners
    @FXML
    private void onClickBtnPreview(ActionEvent event) {
        makeGamePreview(true);
        btnSaveToFile.setDisable(false);
    }

    @FXML
    private void onClickBtnSaveToFile(ActionEvent event) {
        File fileToSave = selectFileToSave();
        if (fileToSave != null) {
            game.saveToFile(previewGameField, fileToSave);
            lastOpenedDirectory = fileToSave.getParentFile();
        }
    }

    @FXML
    private void onClickBtnLoadFromFile() {
        File fileToLoad = selectFileToLoad();
        if (fileToLoad != null) {
            game.loadFromFile(fileToLoad);
            lastOpenedDirectory = fileToLoad.getParentFile();
        }
    }

    @FXML
    private void onClickBtnStart(ActionEvent event) {
        if (mainStage == null) {
            mainStage = (Stage) ((Control) event.getSource()).getScene().getWindow();
        }
        if (isGameRunning) {
            return;
        }
        isGameRunning = true;
        if (gameCanvas == null) {
            initSingleGameCanvas();
        }
        startGame();
    }

    @FXML
    private void onClickBtnStop(ActionEvent event) {
        if (isGameRunning) {
            gameCanvas.stopAnimation();
            unlockUIComponents();
            isGameRunning = false;
        }
    }

    @FXML
    private void onClickFirstLoadFromFileBench(ActionEvent event) {
        File file = selectFileToLoad();
        if (file != null) {
            benchFirstGameField = game.loadFromFile(file);
            if (benchFirstGameField == null) {
                btnFirstLoadFromFileBench.setText("Файл");
                return;
            }
            btnFirstLoadFromFileBench.setText(file.getName());
            int x = benchFirstGameField.getX();
            int y = benchFirstGameField.getY();
            int fill = benchFirstGameField.getFillPercentage();

            fieldFirstXBench.setText(String.valueOf(x));
            fieldFirstYBench.setText(String.valueOf(y));
            fieldFirstFillBench.setText(String.valueOf(fill));

            if (checkSecondSameFieldsBench.isSelected()) {
                duplicateFieldsBenchValues();
            }
        }
    }

    @FXML
    private void onClickSecondLoadFromFileBench(ActionEvent event) {
        File file = selectFileToLoad();
        if (file != null) {
            benchSecondGameField = game.loadFromFile(file);
            if (benchSecondGameField == null) {
                btnSecondLoadFromFileBench.setText("Файл");
                return;
            }
            btnSecondLoadFromFileBench.setText(file.getName());
            int x = benchSecondGameField.getX();
            int y = benchSecondGameField.getY();
            int fill = benchSecondGameField.getFillPercentage();

            fieldFirstXBench.setText(String.valueOf(x));
            fieldFirstYBench.setText(String.valueOf(y));
            fieldFirstFillBench.setText(String.valueOf(fill));
        }
    }

    @FXML
    private void onCheckedSameFieldsBench(ActionEvent event) {
        boolean controlCheckBoxState = checkSecondSameFieldsBench.isSelected();
        if (controlCheckBoxState) {
            setSecondBenchUIElementsState(controlCheckBoxState);
            fieldSecondGensBench.setDisable(true);
            btnSecondLoadFromFileBench.setDisable(true);
        } else {
            if (!checkLoadFileSecondBench.isSelected()) {
                setSecondBenchUIElementsState(controlCheckBoxState);
            } else {
                checkLoadFileSecondBench.setDisable(false);
                btnSecondLoadFromFileBench.setDisable(false);
            }
            fieldSecondGensBench.setDisable(false);
        }

    }

    @FXML
    private void onClickAddToBench(ActionEvent event) {
        ToolBar resultBar;
        if (checkSecondSameFieldsBench.isSelected()) {
            resultBar = sameGameFieldsBenchBar();
        } else {
            resultBar = gameFieldsBenchBar();
        }
        if (resultBar != null) {
            scrollContainer.getChildren().add(resultBar);
        }
        if (!benchmarkItemMap.isEmpty()) {
            btnStartBench.setDisable(false);
        }
    }

    @FXML
    private void onClickBtnStartBench(ActionEvent event) {
        BenchmarkItem firstItem = benchmarkItemMap.get(currentBenchItemIndex);
        new Thread(() -> {
            game.pastGensWithTimeRecording(firstItem.getFieldFirstStart(), firstItem.getGensFirs(), firstItem.getThreadsFirst());
        }).start();

        //TODO лок UI компонентов
    }

    @FXML
    private void onClickBtnStopBench(ActionEvent event) {

    }

    private void initTabs() {
        tabGame.setOnSelectionChanged(event -> {
            currentMode = Mode.GAME;
        });
        tabBenchmark.setOnSelectionChanged(event -> {
            currentMode = Mode.BENCHMARK;
        });
    }

    private void initFields() {
        UnaryOperator<TextFormatter.Change> unaryOperator = change -> {
            String text = change.getText();

            if (text.matches("[0-9]*")) {
                return change;
            }

            return null;
        };

        fieldX.setTextFormatter(new TextFormatter<String>(unaryOperator));
        fieldY.setTextFormatter(new TextFormatter<String>(unaryOperator));
        fieldGens.setTextFormatter(new TextFormatter<String>(unaryOperator));
        fieldFillPercentage.setTextFormatter(new TextFormatter<String>(unaryOperator));
        fieldThreads.setTextFormatter(new TextFormatter<String>(unaryOperator));
        fieldFrameTime.setTextFormatter(new TextFormatter<String>(unaryOperator));

        InvalidationListener invalidationListener = observable -> {
            makeGamePreview(false);
        };

        fieldX.textProperty().addListener(invalidationListener);
        fieldY.textProperty().addListener(invalidationListener);

        fieldFirstXBench.setTextFormatter(new TextFormatter<String>(unaryOperator));
        fieldFirstYBench.setTextFormatter(new TextFormatter<String>(unaryOperator));
        fieldFirstGensBench.setTextFormatter(new TextFormatter<String>(unaryOperator));
        fieldFirstFillBench.setTextFormatter(new TextFormatter<String>(unaryOperator));
        fieldFirstThreadsBench.setTextFormatter(new TextFormatter<String>(unaryOperator));

        InvalidationListener invalidationBenchListener = observable -> {
            if (checkSecondSameFieldsBench.isSelected()) {
                duplicateFieldsBenchValues();
            }
        };

        fieldFirstXBench.textProperty().addListener(invalidationBenchListener);
        fieldFirstYBench.textProperty().addListener(invalidationBenchListener);
        fieldFirstGensBench.textProperty().addListener(invalidationBenchListener);
        fieldFirstFillBench.textProperty().addListener(invalidationBenchListener);
        fieldFirstThreadsBench.textProperty().addListener(invalidationBenchListener);

        fieldSecondXBench.setTextFormatter(new TextFormatter<String>(unaryOperator));
        fieldSecondYBench.setTextFormatter(new TextFormatter<String>(unaryOperator));
        fieldSecondGensBench.setTextFormatter(new TextFormatter<String>(unaryOperator));
        fieldSecondFillBench.setTextFormatter(new TextFormatter<String>(unaryOperator));
        fieldSecondThreadsBench.setTextFormatter(new TextFormatter<String>(unaryOperator));
    }

    private void initCheckBoxes() {
        checkLoadFileFirstBench.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (checkSecondSameFieldsBench.isSelected()) {
                duplicateFieldsBenchValues();
            }
            setFirstBenchUIElementsState(newVal);
        });

        checkLoadFileSecondBench.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (!checkSecondSameFieldsBench.isSelected()) {
                setSecondBenchUIElementsFileLoadState(newVal);
                btnSecondLoadFromFileBench.setDisable(!newVal);
            }
        });
    }

    private void initBenchmarkScrollContainer() {
        scrollContainer = new VBox();
        benchScrollPane.setContent(scrollContainer);
    }

    private void startGame() {
        GameField gameField;
        try {
            int gens = Integer.parseInt(fieldGens.getText());
            int threads = Integer.parseInt(fieldThreads.getText());
            int frameTime = Integer.parseInt(fieldFrameTime.getText());
            if (previewGameField == null || previewGameField.isEmpty()) {
                int x = Integer.parseInt(fieldX.getText());
                int y = Integer.parseInt(fieldY.getText());
                int fill = Integer.parseInt(fieldFillPercentage.getText());
                if (fill > 100) {
                    fill = 100;
                }
                gameField = game.newInstance(x, y);
                game.fillRandom(gameField, fill);
            } else {
                gameField = previewGameField;
            }
            gameCanvas.setFrameTime(frameTime);
            currentTotalGens = gens;
            new Thread(() -> {
                game.pastGensWithTimeRecording(gameField, gens, threads);
            }).start();
            showCalculateProgressWindow();
        } catch (NumberFormatException e) {
            showNumberFormatErrorDialog(e);
            isGameRunning = false;
        }
    }

    private void makeGamePreview(boolean fillField) {
        if (isGameRunning) {
            return;
        }
        btnSaveToFile.setDisable(true);
        String xText = fieldX.getText();
        String yText = fieldY.getText();
        if (gameCanvas == null) {
            initSingleGameCanvas();
        }
        if (xText.length() == 0 || yText.length() == 0) {
            previewGameField = null;
            gameCanvas.hide();
            gameCanvas.clear();
            return;
        }

        int x = Integer.parseInt(fieldX.getText());
        int y = Integer.parseInt(fieldY.getText());
        if (fillField) {
            String fillText = fieldFillPercentage.getText();
            if (fillText.length() == 0) {
                return;
            }
            int fillPercentage = Integer.parseInt(fillText);
            GameField gameField = game.newInstance(x, y);
            game.fillRandom(gameField, fillPercentage);
            previewGameField = gameField;
            gameCanvas.preview(gameField);
        } else {
            previewGameField = null;
            gameCanvas.preview(game.newInstance(x, y));
        }
    }

    private void lockUIComponents() {
        tabBenchmark.setDisable(true);
        fieldX.setDisable(true);
        fieldY.setDisable(true);
        fieldGens.setDisable(true);
        fieldFillPercentage.setDisable(true);
        btnPreview.setDisable(true);
        fieldFrameTime.setDisable(true);
        fieldThreads.setDisable(true);
        btnStart.setDisable(true);
        btnStop.setDisable(false);
        btnSaveToFile.setDisable(true);
        btnLoadFromFIle.setDisable(true);
        lblInfo.setText("Игра заупускается...");
    }

    private void unlockUIComponents() {
        tabBenchmark.setDisable(false);
        fieldX.setDisable(false);
        fieldY.setDisable(false);
        fieldGens.setDisable(false);
        fieldFillPercentage.setDisable(false);
        btnPreview.setDisable(false);
        btnLoadFromFIle.setDisable(false);
        btnSaveToFile.setDisable(false);
        fieldFrameTime.setDisable(false);
        fieldThreads.setDisable(false);
        btnStart.setDisable(false);
        btnStop.setDisable(true);
    }

    private void initGameInstance() {
        game = new TheLifeGame();
        game.getGameEventsPublisher().subscribeListener(this);
    }

    private void initSingleGameCanvas() {
        gameCanvas = new ResizableCanvas(this);
        pane.getChildren().add(gameCanvas);
        AnchorPane.setBottomAnchor(gameCanvas, 0d);
        AnchorPane.setLeftAnchor(gameCanvas, 0d);
        AnchorPane.setTopAnchor(gameCanvas, 40d);
        AnchorPane.setRightAnchor(gameCanvas, 0d);

        int index = pane.getChildren().indexOf(lblInfo);
        pane.getChildren().get(index).toFront();
    }

    private void setFirstBenchUIElementsState(boolean state) {
        fieldFirstXBench.setDisable(state);
        fieldFirstYBench.setDisable(state);
        fieldFirstFillBench.setDisable(state);
        btnFirstLoadFromFileBench.setDisable(!state);
    }

    private void setSecondBenchUIElementsState(boolean state) {
        setSecondBenchUIElementsFileLoadState(state);
        checkLoadFileSecondBench.setDisable(state);
        if (state) {
            duplicateFieldsBenchValues();
        }
    }

    private void setSecondBenchUIElementsFileLoadState(boolean state) {
        fieldSecondXBench.setDisable(state);
        fieldSecondYBench.setDisable(state);
        fieldSecondFillBench.setDisable(state);
    }

    private ToolBar gameFieldsBenchBar() {
        int firstX, firstY, firstFill, firstGens, firstThreads;
        int secondX, secondY, secondFill, secondGens, secondThreads;

        try {
            firstX = Integer.parseInt(fieldFirstXBench.getText());
            firstY = Integer.parseInt(fieldFirstYBench.getText());
            firstFill = Integer.parseInt(fieldFirstFillBench.getText());
            if (checkLoadFileFirstBench.isSelected()) {
                if (benchFirstGameField == null) {
                    showFileNotSelectedErrorDialog();
                    return null;
                }
            } else {
                benchFirstGameField = game.newInstance(firstX, firstY);
                game.fillRandom(benchFirstGameField, firstFill);
            }
            firstGens = Integer.parseInt(fieldFirstGensBench.getText());
            firstThreads = Integer.parseInt(fieldFirstThreadsBench.getText());
        } catch (NumberFormatException e) {
            showNumberFormatErrorDialog(e);
            return null;
        }
        secondX = Integer.parseInt(fieldSecondXBench.getText());
        secondY = Integer.parseInt(fieldSecondYBench.getText());
        secondFill = Integer.parseInt(fieldSecondFillBench.getText());
        if (checkLoadFileSecondBench.isSelected()) {
            if (benchSecondGameField == null) {
                showFileNotSelectedErrorDialog();
                return null;
            }
        } else {
            benchSecondGameField = game.newInstance(secondX, secondY);
            game.fillRandom(benchSecondGameField, secondFill);
        }
        try {
            secondGens = Integer.parseInt(fieldSecondGensBench.getText());
            secondThreads = Integer.parseInt(fieldSecondThreadsBench.getText());
        } catch (NumberFormatException e) {
            showNumberFormatErrorDialog(e);
            return null;
        }

        benchmarkItemMap.put(benchElementsIndex, new BenchmarkItem(benchFirstGameField, benchSecondGameField, firstGens, secondGens, firstThreads, secondThreads));
        return getBenchBar(benchFirstGameField, benchSecondGameField, firstGens, secondGens, firstThreads, secondThreads);
    }

    private ToolBar sameGameFieldsBenchBar() {
        if (checkLoadFileFirstBench.isSelected()) {
            if (benchFirstGameField == null) {
                showFileNotSelectedErrorDialog();
                return null;
            }
            benchSecondGameField = benchFirstGameField.copy();
            try {
                int gens = Integer.parseInt(fieldFirstGensBench.getText());
                int threadsFirs = Integer.parseInt(fieldFirstThreadsBench.getText());
                int threadsSecond = Integer.parseInt(fieldSecondThreadsBench.getText());

                benchmarkItemMap.put(benchElementsIndex, new BenchmarkItem(benchFirstGameField, benchSecondGameField, gens, gens, threadsFirs, threadsSecond));
                return getBenchBar(benchFirstGameField, benchSecondGameField, gens, gens, threadsFirs, threadsSecond);
            } catch (NumberFormatException e) {
                showNumberFormatErrorDialog(e);
                return null;
            }
        } else {
            try {
                int x = Integer.parseInt(fieldFirstXBench.getText());
                int y = Integer.parseInt(fieldFirstYBench.getText());
                int fill = Integer.parseInt(fieldFirstFillBench.getText());
                GameField firstGameField = game.newInstance(x, y);
                game.fillRandom(firstGameField, fill);
                GameField secondGameField = game.copyFrom(firstGameField);
                int gens = Integer.parseInt(fieldFirstGensBench.getText());
                int threadsFirs = Integer.parseInt(fieldFirstThreadsBench.getText());
                int threadsSecond = Integer.parseInt(fieldSecondThreadsBench.getText());

                benchmarkItemMap.put(benchElementsIndex, new BenchmarkItem(firstGameField, secondGameField, gens, gens, threadsFirs, threadsSecond));
                return getBenchBar(firstGameField, secondGameField, gens, gens, threadsFirs, threadsSecond);
            } catch (NumberFormatException e) {
                showNumberFormatErrorDialog(e);
                return null;
            }
        }
    }

    private ToolBar getBenchBar(GameField firstGameField, GameField secondGameField, int firstGens, int secondGens, int firstThreads, int secondThreads) {
        double timeSize = 100;
        Insets smallPadding = new Insets(0, 0, 0, 15);
        Insets mainPadding = new Insets(0, 0, 0, 25d);
        Insets bigPadding = new Insets(0, 0, 0, 50d);

        //index 0
        Label indexNumber = new Label(String.valueOf(benchElementsIndex));
        indexNumber.setFont(Font.font("System", FontWeight.BOLD, 15));
        indexNumber.paddingProperty().setValue(smallPadding);
        benchElementsIndex++;

        StringBuilder sb = new StringBuilder();
        sb.append("Размер: ")
                .append(firstGameField.getX()).append('x').append(firstGameField.getY()).append(". ")
                .append("Заполнение: ").append(firstGameField.getFillPercentage()).append("% ")
                .append("Поколений: ").append(firstGens).append(". ")
                .append("Потоков: ").append(firstThreads);
        //index 1
        Label fieldFirstInfo = new Label(sb.toString());
        fieldFirstInfo.paddingProperty().setValue(mainPadding);

        //index 2
        Label firstResultLabel = new Label();
        firstResultLabel.setPrefWidth(timeSize);
        firstResultLabel.setPadding(mainPadding);

        //index 3
        ProgressBar firstProgressBar = new ProgressBar(0.0d);
        firstProgressBar.setPrefWidth(100d);
        firstProgressBar.setPadding(mainPadding);
        firstProgressBar.setVisible(true);

        sb = new StringBuilder();
        sb.append("Размер: ")
                .append(secondGameField.getX()).append('x').append(secondGameField.getY()).append(". ")
                .append("Заполнение: ").append(secondGameField.getFillPercentage()).append("% ")
                .append("Поколений: ").append(secondGens).append(". ")
                .append("Потоков: ").append(secondThreads);
        //index 4
        Label fieldSecondInfo = new Label(sb.toString());
        fieldSecondInfo.paddingProperty().setValue(bigPadding);

        //index 5
        Label secondResultLabel = new Label("Время: 4911 мс");
        secondResultLabel.setPrefWidth(timeSize);
        secondResultLabel.setPadding(mainPadding);

        //index 6
        ProgressBar secondProgressBar = new ProgressBar(0.0d);
        secondProgressBar.setPrefWidth(100d);
        secondProgressBar.setPadding(mainPadding);
        secondProgressBar.setVisible(true);

        //index 7
        Button btnDelete = new Button("Удалить");
        btnDelete.setStyle(
                "    -fx-background-color: linear-gradient(#ff5400, #be1d00);" +
                        "    -fx-background-radius: 30;" +
                        "    -fx-background-insets: 0;" +
                        "    -fx-text-fill: white;");
        btnDelete.setTranslateX(25);


        return new ToolBar(indexNumber, fieldFirstInfo, firstResultLabel, firstProgressBar, fieldSecondInfo, secondResultLabel, secondProgressBar, btnDelete);
    }

    private void searchAndRunNextGame() {
        GameField secondEnd = currentItem.getFieldSecondEnd();
        if (secondEnd == null) {
            new Thread(() -> {
                game.pastGensWithTimeRecording(currentItem.getFieldSecondStart(), currentItem.getGensSecond(), currentItem.getThreadsSecond());
            }).start();

        } else {
            currentBenchItemIndex++;
            currentItem = benchmarkItemMap.get(currentBenchItemIndex);
            if (currentItem == null) {
                makeDialogWindow(Alert.AlertType.INFORMATION, "Тест завершен",
                        "Тест завершился успешно",
                        "Всего пар было сравнено: " + (currentBenchItemIndex - 1)).showAndWait();
            } else {
                new Thread(() -> {
                    game.pastGensWithTimeRecording(currentItem.getFieldFirstStart(), currentItem.getGensFirs(), currentItem.getThreadsFirst());
                }).start();
            }
        }
    }

    private void benchGameStarted(boolean isFirst) {
        int indexShift = isFirst ? 0 : 3;
        currentTotalGens = isFirst ? currentItem.getGensFirs() : currentItem.getGensSecond();
        ToolBar toolBar = (ToolBar) scrollContainer.getChildren().get(currentBenchItemIndex - 1);
        currentBenchProgressBar = (ProgressBar) toolBar.getItems().get(BENCH_FIRST_PROGRESSBAR_INDEX + indexShift);
        currentTimeLabel = (Label) toolBar.getItems().get(BENCH_FIRST_RESULT_INDEX + indexShift);
        currentBenchProgressBar.setVisible(true);
        currentTimeLabel.setVisible(true);
        currentTimeLabel.setText("");
    }

    private void duplicateFieldsBenchValues() {
        fieldSecondXBench.setText(fieldFirstXBench.getText());
        fieldSecondYBench.setText(fieldFirstYBench.getText());
        fieldSecondGensBench.setText(fieldFirstGensBench.getText());
        fieldSecondFillBench.setText(fieldFirstFillBench.getText());
        checkLoadFileSecondBench.setSelected(checkLoadFileFirstBench.isSelected());
        btnSecondLoadFromFileBench.setText(btnFirstLoadFromFileBench.getText());
    }

    private File selectFileToSave() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить в файл");
        if (lastOpenedDirectory != null) {
            fileChooser.setInitialDirectory(lastOpenedDirectory);
        }

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        return fileChooser.showSaveDialog(mainStage);
    }

    private File selectFileToLoad() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Загрузить из файла");
        if (lastOpenedDirectory != null) {
            fileChooser.setInitialDirectory(lastOpenedDirectory);
        }
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        return fileChooser.showOpenDialog(mainStage);
    }

    private void showCalculateProgressWindow() {
        calculateProgressBar = new ProgressBar(0d);
        AnchorPane.setBottomAnchor(calculateProgressBar, 0d);
        AnchorPane.setLeftAnchor(calculateProgressBar, 0d);
        AnchorPane.setTopAnchor(calculateProgressBar, 0d);
        AnchorPane.setRightAnchor(calculateProgressBar, 0d);
        Parent root = new AnchorPane(calculateProgressBar);
        Scene scene = new Scene(root);
        calculateStage = new Stage(StageStyle.UNDECORATED);
        calculateStage.setScene(scene);
        calculateStage.setWidth(250d);
        calculateStage.setHeight(40);
        calculateStage.initModality(Modality.WINDOW_MODAL);
        calculateStage.initOwner(mainStage);
        calculateStage.setX(mainStage.getX() + mainStage.getWidth() / 2 - 125);
        calculateStage.setY(mainStage.getY() + mainStage.getHeight() / 2 - 20);
        calculateStage.show();
    }

    private void showFileNotSelectedErrorDialog() {
        makeDialogWindow(Alert.AlertType.ERROR, "Ошибка!",
                "Файл не выбран!",
                "Выберите файл сохранённого состояния игры или уберите галочку с соответствущего поля!").showAndWait();
    }

    private void showNumberFormatErrorDialog(Exception e) {
        makeDialogWindow(Alert.AlertType.ERROR, "Ошибка!",
                e.getMessage(),
                "Поля не могут быть пустыми!").showAndWait();
    }

    private Alert makeDialogWindow(Alert.AlertType type, String title, String header, String context) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(context);
        return alert;
    }

    private enum Mode {
        GAME, BENCHMARK
    }
}
