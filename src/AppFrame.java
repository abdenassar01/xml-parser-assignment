import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class AppFrame extends JFrame implements ActionListener {
   
   Container container;
   
   //panels
       JPanel main = new JPanel();
       JPanel right = new JPanel();
       JPanel rightForm = new JPanel();
       JPanel namePanel = new JPanel();
       JPanel lastnamePanel = new JPanel();
       JPanel addressPanel = new JPanel();
       JPanel mailPanel = new JPanel();
       JPanel telePanel = new JPanel();
   
   //buttons
        JButton save = new JButton("Ajouter Element");
        JButton load = new JButton("Charger Fichier");
        JFileChooser fileChooser = new JFileChooser();
        
   // label and text fields
        JTextField name = new JTextField(); 
        JTextField lastname = new JTextField(); 
        JTextField address = new JTextField(); 
        JTextField mail = new JTextField(); 
        JTextField telephone = new JTextField();
        JLabel nameLabel = new JLabel("Nom");
        JLabel lastnameLabel = new JLabel("Prenom");
        JLabel mailLabel = new JLabel("Email");
        JLabel addressLabel = new JLabel("Adresse");
        JLabel teleLabel = new JLabel("Telephone");
        
        GridLayout grid = new GridLayout();
        
    //table
        JTable table;


    String[] columnNames = {
            "Nom",
            "Prenom",
            "Adresse",
            "Mail",
            "Telephone"
    };
    
    DefaultTableModel model;
    public AppFrame() throws HeadlessException {
        
        table = new JTable();
        model = (DefaultTableModel) table.getModel();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("XML file","xml");
        fileChooser.setFileFilter(filter);
        
        container = this.getContentPane();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        
        namePanel.setLayout(grid);
        namePanel.add(nameLabel);
        namePanel.add(name);
        
        lastnamePanel.setLayout(grid);
        lastnamePanel.add(lastnameLabel);
        lastnamePanel.add(lastname);
        
        addressPanel.setLayout(grid);
        addressPanel.add(addressLabel);
        addressPanel.add(address);
        
        mailPanel.setLayout(grid);
        mailPanel.add(mailLabel);
        mailPanel.add(mail);
        
        telePanel.setLayout(grid);
        telePanel.add(teleLabel);
        telePanel.add(telephone);

        GridLayout layout = new GridLayout(7, 1);
        rightForm.setLayout(new GridLayout(2, 1));
        layout.setVgap(5);
        rightForm.setLayout(layout);
        rightForm.add(namePanel);
        rightForm.add(lastnamePanel);
        rightForm.add(addressPanel);
        rightForm.add(mailPanel);
        rightForm.add(telePanel);
        rightForm.add(save);
        rightForm.add(save);
        rightForm.add(load);
        right.setLayout(new GridLayout(2, 1));
        right.add(rightForm);
        right.add(new JScrollPane(table));
        
        main.setLayout(new GridLayout(1, 2));
        main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        main.add(right);
        
        container.add(main);
        
        save.addActionListener(this);
        load.addActionListener(this);
    }
    

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == load) {
            
            int returnVal = fileChooser.showOpenDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();

                loadFile(file.getName());
            } else {
                System.out.println("Utilisateur a annule l'ouverture de fichier.");
            }
        } else if (e.getSource() == save) {
            DocumentBuilder builder = null;
            try {
                builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document doc = builder.parse(new File("src/files/personne.xml"));
                doc.getDocumentElement().normalize();
                
                Node root = doc.getElementsByTagName("Personnes").item(0);
                
                Node person = doc.createElement("Personne");
                Node nomText = doc.createElement("nom");
                Node prenomText = doc.createElement("prenom");
                Node addressText = doc.createElement("adresse");
                Node teleText = doc.createElement("telephone");
                Node mailText = doc.createElement("mail");
                
                nomText.setTextContent(name.getText());
                prenomText.setTextContent(lastname.getText());
                addressText.setTextContent(address.getText());
                mailText.setTextContent(mail.getText());
                teleText.setTextContent(telephone.getText());
                
                person.appendChild(nomText);
                person.appendChild(prenomText);
                person.appendChild(addressText);
                person.appendChild(mailText);
                person.appendChild(teleText);
                
                root.appendChild(person);

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);

                StreamResult result = new StreamResult("src/files/personne.xml");
                transformer.transform(source, result);
                
                loadFile("personne.xml");
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            
        }
    }

    private void loadFile(String path) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new File("src/files/" + path));
            doc.getDocumentElement().normalize();
            
            model.addColumn("Nom");
            model.addColumn("Prenom");
            model.addColumn("Adresse");
            model.addColumn("Mail");
            model.addColumn("Telephone");
            model.addColumn("sexe");
            
            NodeList names = doc.getElementsByTagName("nom");
            NodeList prenames = doc.getElementsByTagName("prenom");
            NodeList adresses = doc.getElementsByTagName("adresse");
            NodeList mails = doc.getElementsByTagName("mail");
            NodeList teles = doc.getElementsByTagName("telephone");
            NodeList personnes = doc.getElementsByTagName("Personne");
            
            for (int i = 0; i < personnes.getLength() ; i++) {
                model.addRow(new Object[] {
                        names.item(i).getTextContent(),
                        prenames.item(i).getTextContent(),
                        adresses.item(i).getTextContent(),
                        mails.item(i).getTextContent(),
                        teles.item(i).getTextContent(),
                        personnes.item(i).getAttributes().getNamedItem("sexe").getTextContent()
                });
            }
            
        }catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
