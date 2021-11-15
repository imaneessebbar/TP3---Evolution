package metrics;

/**@author ES-SEBBAR Imane
 * @author EL MAROUNI Majda
 * */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.ASTVisitor;

import parsers.EclipseJDTASTParser;
import main.CallGraphMain;
import java.util.ArrayList;

import parsers.EclipseJDTASTParser;

// to extract the classe's names of each package
public class Classes {
	public static ArrayList classes = new ArrayList();
	private static String PackageClasses = "";
	private static ArrayList AllPackagesClasses = new ArrayList();

	
	public static ArrayList<String> getPackageClasses() throws IOException{
		
		EclipseJDTASTParser eJDTASTParser = new EclipseJDTASTParser(CallGraphMain.TEST_PROJECT_PATH);
        List<File> projectFiles = eJDTASTParser.listJavaProjectFiles();

        for (File classpath : projectFiles){
            String classPath = classpath.toString();
            String[] s;
            if(classPath.contains("\\")){
                s = classPath.split("\\\\");
            }
            else {
                s = classPath.split("/");
            }
            String class_ = s[s.length-1];
            if(!classes.contains(class_))
                classes.add(class_);
            PackageClasses= class_;
            s = s[s.length-1].split("[.]");
            PackageClasses = s[0];

            final CompilationUnit cu = eJDTASTParser.parse(classpath) ;
            cu.accept(new ASTVisitor() {
				Set names = new HashSet();
				public boolean visit(PackageDeclaration node) {
					Name name = node.getName();
                    PackageClasses = name+"."+PackageClasses;
					return false; 
				}
			});
            if(!AllPackagesClasses.contains(PackageClasses))
            	AllPackagesClasses.add(PackageClasses);
        }
		return AllPackagesClasses;
		
	}
}
