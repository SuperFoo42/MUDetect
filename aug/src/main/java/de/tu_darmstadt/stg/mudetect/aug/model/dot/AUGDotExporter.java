package de.tu_darmstadt.stg.mudetect.aug.model.dot;

import com.google.common.io.Files;
import de.tu_darmstadt.stg.mudetect.aug.model.APIUsageExample;
import de.tu_darmstadt.stg.mudetect.aug.model.APIUsageGraph;
import de.tu_darmstadt.stg.mudetect.aug.model.Edge;
import de.tu_darmstadt.stg.mudetect.aug.model.Node;
import de.tu_darmstadt.stg.mudetect.aug.visitors.AUGLabelProvider;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.IntegerIdProvider;
import org.jgrapht.nio.dot.DOTExporter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import java.util.function.Function;

public class AUGDotExporter {
    private static final String GRAPHVIZ_PATH = System.getenv("Graphviz");
    private static final String WINDOWS_EXEC_DOT = GRAPHVIZ_PATH+"/dot.exe"; //"D:/Program Files (x86)/Graphviz2.36/bin/dot.exe";	// Windows
    private static final String LINUX_EXEC_DOT = "dot";	// Linux
    private static String EXEC_DOT = null;

    static {
        if (System.getProperty("os.name").startsWith("Windows"))
            // checking whether Grpahviz path was set in environment variable
            if(GRAPHVIZ_PATH!=null){
                EXEC_DOT = WINDOWS_EXEC_DOT;
            }
            else{
                System.err.println("No environment variable 'Graphviz' found targeting the installation path of dot.exe");
            }
        else
            EXEC_DOT = LINUX_EXEC_DOT;
    }

    private static final String NEW_LINE = System.getProperty("line.separator");

    private IntegerIdProvider<Node> nodeIdProvider = new IntegerIdProvider<>();
    private final DOTExporter<Node, Edge> exporter;
    private final AUGAttributeProvider<APIUsageGraph> graphAttributeProvider;

    public AUGDotExporter(AUGLabelProvider labelProvider,
                          Function<Node,Map<java.lang.String, Attribute>> nodeAttributeProvider,
                          Function<Edge,Map<java.lang.String, Attribute>>  edgeAttributeProvider) {
        this(labelProvider::getLabel, labelProvider::getLabel, nodeAttributeProvider, edgeAttributeProvider);
    }

    public AUGDotExporter(Function<Node, String> nodeLabelProvider,
                          Function<Edge, String> edgeLabelProvider,
                          Function<Node,Map<java.lang.String, Attribute>> nodeAttributeProvider,
                          Function<Edge,Map<java.lang.String, Attribute>>  edgeAttributeProvider) {
        this(nodeLabelProvider, edgeLabelProvider, nodeAttributeProvider, edgeAttributeProvider, null);
    }

    public AUGDotExporter(Function<Node, String> nodeLabelProvider,
                          Function<Edge, String> edgeLabelProvider,
                          Function<Node,Map<java.lang.String, Attribute>> nodeAttributeProvider,
                          Function<Edge,Map<java.lang.String, Attribute>>  edgeAttributeProvider,
                          AUGAttributeProvider<APIUsageGraph> graphAttributeProvider) {
        this.exporter = new DOTExporter<>(nodeIdProvider);
        this.exporter.setVertexAttributeProvider(nodeAttributeProvider);
        this.exporter.setEdgeAttributeProvider(edgeAttributeProvider);
        this.graphAttributeProvider = graphAttributeProvider;
    }

    public String toDotGraph(APIUsageGraph aug) {
        StringWriter writer = new StringWriter();
        toDotGraph(aug, writer);
        return writer.toString();
    }

    private void toDotGraph(APIUsageGraph aug, Writer writer) {
        nodeIdProvider = new IntegerIdProvider<>();
        exporter.exportGraph(aug, new PrintWriter(writer) {
            @Override
            public void write(String s, int off, int len) {
                if (s.equals("digraph G {")) {
                    String methodName = aug instanceof APIUsageExample ? ((APIUsageExample) aug).getLocation().getMethodSignature() : "AUG";
                    StringBuilder data = new StringBuilder("digraph \"").append(methodName).append("\" {").append(NEW_LINE);
                    if (graphAttributeProvider != null) {
                        for (Map.Entry<String, String> attribute : graphAttributeProvider.getAUGAttributes(aug).entrySet()) {
                            data.append(attribute.getKey()).append("=").append(attribute.getValue()).append(";").append(NEW_LINE);
                        }
                    }
                    super.write(data.toString(), 0, data.length());
                } else {
                    super.write(s, off, len);
                }
            }
        });
    }

    public void toDotFile(APIUsageGraph aug, File file) throws IOException {
        if (!file.getPath().endsWith(".dot")) {
            file = new File(file.getPath() + ".dot");
        }
        file = file.getAbsoluteFile();
        ensureDirectory(file.getParentFile());
        try (BufferedWriter fout = new BufferedWriter(new FileWriter(file))) {
            fout.append(toDotGraph(aug));
            fout.flush();
        }
    }

    public void toPNGFile(APIUsageGraph aug, File file) throws IOException, InterruptedException {
        file = file.getAbsoluteFile();
        File directory = file.getParentFile();
        ensureDirectory(directory);
        String nameWithoutExtension = new File(directory, Files.getNameWithoutExtension(file.getPath())).getPath();
        toDotFile(aug, new File(nameWithoutExtension + ".dot"));
        Runtime rt = Runtime.getRuntime();
        Process p = rt.exec(new String[]{EXEC_DOT, "-T"+ "png", nameWithoutExtension +".dot", "-o", nameWithoutExtension +"."+ "png"});
        p.waitFor();
    }

    private void ensureDirectory(File path) {
        if (!path.exists()) path.mkdirs();
    }
}
