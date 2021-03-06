package metrics;

/**@author ES-SEBBAR Imane
 * @author EL MAROUNI Majda
 * */
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

//import com.ibm.icu.text.DecimalFormat;
//import com.ibm.icu.text.NumberFormat;

import org.eclipse.jdt.internal.core.search.matching.MatchLocatorParser.ClassAndMethodDeclarationVisitor;

import graphs.CallGraph;

public class Metric {
	static HashMap<String, Integer> ClassAClassBNbCall = new HashMap<>();
	static HashMap<String, Float> ClassAClassBMetric = new HashMap<>();
	//static ArrayList<String> classes = new ArrayList();
	static float AllCalls = 0;
	
	
	public static void getClassAClassBNbCalls(CallGraph graph) throws IOException{
		List PackageClasses = Classes.getPackageClasses();
		System.out.println("PackageClasses.size : "+ PackageClasses.size());
		Map<String, Map<String,Integer>> invocations = graph.getInvocations();
		System.out.println("invocations.size : "+ invocations.size());
		for(String source: invocations.keySet()) {
			String classA = getPackageClass(source);
			if(PackageClasses.contains(classA)) {
				for(String destination : invocations.get(source).keySet()) {
					String classB = getPackageClass(destination);
						int callsBetweenAB = invocations.get(source).get(destination);
						String classAclassB = classA+"_"+classB;
					if(!classA.equals(classB) && PackageClasses.contains(classB) ) {
						if(ClassAClassBNbCall.get(classAclassB)!=null) {
							int p = ClassAClassBNbCall.get(classAclassB);
							callsBetweenAB += p;
						}
						ClassAClassBNbCall.put(classAclassB, callsBetweenAB);

					}

				}
			}
		}
	}
	
	public static void print(CallGraph graph) throws IOException {
		getClassAClassBNbCalls(graph);
		System.out.println("Le nombre d'appels entre les classes : ");
		for(String classAclassB : ClassAClassBNbCall.keySet()) {
			System.out.println("("+classAclassB+")" + " :: " + ClassAClassBNbCall.get(classAclassB) );
		}
		System.out.println();
		CouplageMetric();
		System.out.println("La metrique de couplage entre les classes : ");
		for(String classAclassB : ClassAClassBMetric.keySet()) {
			System.out.println("("+classAclassB+")" + " :: " + ClassAClassBMetric.get(classAclassB) );
		}
		System.out.println();

		
	}
	public static String getPackageClass(String name) {
		return name.split("::")[0];
	}
	public static  String getClass(String ClassAClassB,int i){ // i = 0 : ClassA; i = 1 : ClassB
        String class_ = "";
        String[] s = ClassAClassB.split("_");
        String source = s[i];
        if(source.contains(".")){
            s = source.split("[.]");
            source = s[s.length-1];
        }
        class_ = source ; 
        return class_;
    }
	public static void CouplageMetric() {
		setAllCalls();
		float metric = 0;
		if(AllCalls!=0) {
			String classA;
			String classB;
			for (String ClassAClassB : ClassAClassBNbCall.keySet()){
				metric = ClassAClassBNbCall.get(ClassAClassB)/AllCalls;
				classA = getClass(ClassAClassB, 0);
				classB = getClass(ClassAClassB, 1);
				ClassAClassBMetric.put(classA+","+classB, metric);
			}
		}
		
	}
	private static void setAllCalls() {
		for (String ClassAClassB : ClassAClassBNbCall.keySet()){
			AllCalls += ClassAClassBNbCall.get(ClassAClassB);
        }
	}
	
}
