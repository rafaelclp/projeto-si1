package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class LeitorArquivo {

	// Lista de disciplinas obtidas do arquivo
    private final List<Disciplina> disciplinas;

    /**
     * Construtor
     * 
     */
    public LeitorArquivo() {
        disciplinas = new ArrayList<Disciplina>();
    }

    /**
     * Metodo responsavel por carregar disciplinas, retorna lista com todas as
     * disciplinas contidas no arquivo XML
     * 
     * @return Set das disciplinas criadas apartir do arquivo XML
     */
    public List<Disciplina> carregaDisciplinas() {
        this.populaDisciplinas();
        return this.disciplinas;
    }

    /**
     * Metodo responsavel por verificar cada disciplina no arquivo e iniciar o
     * processo de criacao da mesma
     */
    private void populaDisciplinas() {
        try {
            Document doc = parserXML();
            NodeList nList = doc.getElementsByTagName("cadeira");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nodeDisciplina = nList.item(temp);
                if (nodeDisciplina.getNodeType() == Node.ELEMENT_NODE) {
                    criaDisciplina(nodeDisciplina);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Cria disciplina com os dados retirados de cada Element cadeira presente
     * no arquivo XML
     * 
     * @param nodeDisciplina
     *            node da arvore o qual possui dados de uma disciplina
     */
    private void criaDisciplina(Node nodeDisciplina) {

        Element cadeiraXML = (Element) nodeDisciplina;
        // Atributo da nova Disciplina
        String nome = cadeiraXML.getAttribute("nome");
        String id = cadeiraXML.getAttribute("id");
        int dificuldade = Integer.parseInt(cadeiraXML
                .getElementsByTagName("dificuldade").item(0).getTextContent());
        int creditos = Integer.parseInt(cadeiraXML
                .getElementsByTagName("creditos").item(0).getTextContent());
        int periodo = Integer.parseInt(cadeiraXML
                .getElementsByTagName("periodo").item(0).getTextContent());
        // Cria Disciplina com atributos
        Disciplina novaDisciplina = new Disciplina(nome, creditos, dificuldade,
                periodo, Integer.parseInt(id));

        // List com todos os IDs dos pre-requisitos de disciplina
        NodeList requisitos = cadeiraXML.getElementsByTagName("id");
        // para cada elemento em requisitos, adiciona o mesmo como pre-requisito
        // a disciplina
        for (int i = 0; i < requisitos.getLength(); i++) {
            for (Disciplina dis : disciplinas) {
                if (("" + dis.getID()).equals(requisitos.item(i)
                        .getTextContent())) {
                    novaDisciplina.addPreRequisito(dis);
                }
            }
        }
        // adiciona disciplina criada a lista de disciplinas
        disciplinas.add(novaDisciplina);

    }

    /**
     * Carrega arquivo XML, e cria arvore a partir do mesmo retornarndo um
     * parser utilizado para retirar informacoes de cada disciplina
     * registrada no arquivo.
     * 
     * @return Parser criado a partir do arquivo XML com dados das disciplinas
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    private static Document parserXML() throws ParserConfigurationException,
            SAXException, IOException {
        // Carrega XML
        File arquivoXML = new File("/cadeiras.xml");
        // Cria XML parser retorna DOM trees
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        // XML Document normalizado
        Document parser = dBuilder.parse(arquivoXML);
        parser.getDocumentElement().normalize();

        return parser;
    }
}
