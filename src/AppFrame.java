import org.w3c.dom.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

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
        JPanel radioPanel = new JPanel();
        JPanel sexePanel = new JPanel();

    //buttons
        JButton save = new JButton("Ajouter Element");
        JButton load = new JButton("Charger Fichier");
        JButton suppremer = new JButton("suppremer");
        JButton genererHtml = new JButton("Generer HTML");

        JFileChooser fileChooser = new JFileChooser();
        JRadioButton homme = new JRadioButton("H");
        JRadioButton femme = new JRadioButton("F");
        ButtonGroup radios = new ButtonGroup();
        
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
        JLabel sexeLabel = new JLabel("Sexe: ");
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
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        
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

        radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.X_AXIS));
        radios.add(homme);
        radios.add(femme);
        homme.setActionCommand("Homme");
        femme.setActionCommand("Femme");
        radioPanel.add(homme);
        radioPanel.add(femme);
        sexePanel.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
        sexePanel.setLayout(grid);
        sexePanel.add(sexeLabel);
        sexePanel.add(radioPanel);

        GridLayout layout = new GridLayout(10, 1);
        rightForm.setLayout(new GridLayout(2, 1));
        layout.setVgap(5);
        rightForm.setLayout(layout);
        rightForm.add(namePanel);
        rightForm.add(lastnamePanel);
        rightForm.add(addressPanel);
        rightForm.add(mailPanel);
        rightForm.add(telePanel);
        rightForm.add(sexePanel);
        rightForm.add(save);
        rightForm.add(load);
        rightForm.add(genererHtml);
        rightForm.add(suppremer);
        right.setLayout(new GridLayout(2, 1));
        right.add(rightForm);
        right.add(new JScrollPane(table));
        main.setLayout(new GridLayout(1, 2));
        main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        main.add(right);
        container.add(main);

        save.addActionListener(this);
        load.addActionListener(this);
        genererHtml.addActionListener(this);
        suppremer.addActionListener(this);
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
        } else if (e.getSource() == suppremer) {
            if(table.getSelectedRow() == -1){
                JOptionPane.showMessageDialog(null, "Tu doit obligatoirement selectioner un colone dans le tableau");
                return;
            }

            try {
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document doc = builder.parse(new File("src/files/personne.xml"));
                doc.getDocumentElement().normalize();
                Node personne = doc.getElementsByTagName("Personne").item(table.getSelectedRow());
                personne.getParentNode().removeChild(personne);
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer t = tf.newTransformer();
                t.transform(new DOMSource(doc), new StreamResult("src/files/personne.xml"));
                loadFile("personne.xml");
            }catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            
        } else if (e.getSource() == genererHtml){
            try {
                TransformerFactory tFactory=TransformerFactory.newInstance();

                Source xslDoc = new StreamSource("src/files/personne.xsl");
                Source xmlDoc = new StreamSource("src/files/personne.xml");

                String outputFileName="personne.html";

                OutputStream htmlFile=new FileOutputStream(outputFileName);
                Transformer trasform=tFactory.newTransformer(xslDoc);
                trasform.transform(xmlDoc, new StreamResult(htmlFile));
            }catch (Exception ex){
                System.out.println(ex.getMessage());
            }
        }else if (e.getSource() == save) {
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
                ((Element)person).setAttribute("sexe",radios.getSelection().getActionCommand());

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
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);
            
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new File("src/files/" + path));
            doc.getDocumentElement().normalize();
            
            if(model.getColumnCount() == 0) {
                model.addColumn("Nom");
                model.addColumn("Prenom");
                model.addColumn("Adresse");
                model.addColumn("Mail");
                model.addColumn("Telephone");
                model.addColumn("sexe");
            }
            
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
