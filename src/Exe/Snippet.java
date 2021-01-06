package Exe;

import Interfaz.Compilador;
import Managers.Propiedades;
import Managers.WorkspaceManager;

import java.awt.*;
import java.io.File;
import java.net.URISyntaxException;
import java.util.Objects;

import static Managers.UI.load;


public class Snippet
{
	
	public static Propiedades propierties;

	public static String currentPath;

	public static void main(String[] args) {

//		DataDefinition.build(3,2);
		try {
//			UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");

			String protocol = Snippet.class.getResource("").getProtocol();
			if(Objects.equals(protocol, "jar")){
				System.out.println("runnig a jar");

				currentPath = new File(Snippet.class.getProtectionDomain().getCodeSource().getLocation
						().toURI()).getParent();

			} else if(Objects.equals(protocol, "file")) {
				System.out.println("Running from files/ide");

				currentPath = new File(Snippet.class.getProtectionDomain().getCodeSource().getLocation
						().toURI()).getParentFile().getParentFile().getParent();
			}



			//This works for jar executable
			System.out.println("Current path: "+currentPath);

			initProgram(args);
		} catch (URISyntaxException  e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	private static void initProgram(String args[])
	{

		EventQueue.invokeLater(() -> {
			try {

//				EstadoGramatica.cargar();
//				DefinicionesLenguaje.getER();
				load();
				new WorkspaceManager(
						(a)->
						{
							System.out.println("cheking space args: "+args.length);
							new Compilador().setVisible(true);

							if(args.length>0)
							{
								for(String path : args)
									Compilador.getInstance().loadDocument(new File(path));
							}
						}
				);

			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

}

