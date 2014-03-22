package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CarregadorDeDisciplinas {

	private static Map<String, List<Disciplina>> cache = new TreeMap<String, List<Disciplina>>();

    /**
     * Carrega disciplinas e retorna lista com todas as
     * disciplinas contidas no arquivo XML
     * 
     * @return Set das disciplinas criadas apartir do arquivo XML
     */
    public static List<Disciplina> carregaDisciplinas(String arquivo) {
    	if (!cache.containsKey(arquivo)) {
    		 cache.put(arquivo, carregaDisciplinasDoArquivo(arquivo));
    	}
    	return cache.get(arquivo);
    }

    public static List<Disciplina> carregaDisciplinas(TipoDeGrade tipoDeGrade) {
    	String arquivo = null;
    	if (tipoDeGrade == TipoDeGrade.FLUXOGRAMA_OFICIAL) {
    		arquivo = "cadeiras.xml";
    	}

    	return carregaDisciplinas(arquivo);
    }

    /**
     * Verifica cada disciplina no arquivo e inicia o
     * processo de criacao da mesma
     */
    private static List<Disciplina> carregaDisciplinasDoArquivo(String arquivo) {
    	List<Disciplina> disciplinas = new ArrayList<Disciplina>();

    	try {
            Document doc = parserXML(arquivo);
            NodeList nList = doc.getElementsByTagName("cadeira");
            
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nodeDisciplina = nList.item(temp);
                if (nodeDisciplina.getNodeType() == Node.ELEMENT_NODE) {
                    disciplinas.add(criaDisciplina(disciplinas, nodeDisciplina));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    	
    	return disciplinas;
    }

    /**
     * Cria disciplina com os dados retirados de cada Element cadeira presente
     * no arquivo XML
     * 
     * @param nodeDisciplina
     *            node da arvore o qual possui dados de uma disciplina
     */
    private static Disciplina criaDisciplina(List<Disciplina> disciplinas, Node nodeDisciplina) {
        Element cadeiraXML = (Element) nodeDisciplina;
        
        String nome = cadeiraXML.getAttribute("nome");
        int id = Integer.parseInt(cadeiraXML.getAttribute("id"));
        int dificuldade = Integer.parseInt(cadeiraXML
                .getElementsByTagName("dificuldade").item(0).getTextContent());
        int creditos = Integer.parseInt(cadeiraXML
                .getElementsByTagName("creditos").item(0).getTextContent());
        int periodo = Integer.parseInt(cadeiraXML
                .getElementsByTagName("periodo").item(0).getTextContent());
        
        Disciplina novaDisciplina = new Disciplina(nome, creditos, dificuldade,
                periodo, id);

        NodeList requisitos = cadeiraXML.getElementsByTagName("id");
        
        for (int i = 0; i < requisitos.getLength(); i++) {
            for (Disciplina dis : disciplinas) {
                if (("" + dis.getId()).equals(requisitos.item(i).getTextContent())) {
                    novaDisciplina.addPreRequisito(dis);
                    dis.addPosRequisito(novaDisciplina);
                }
            }
        }

        return novaDisciplina;
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
    private static Document parserXML(String arquivo) throws ParserConfigurationException,
            SAXException, IOException {
        
    	File arquivoXML = new File(arquivo);
        
    	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        
        Document parser = dBuilder.parse(arquivoXML);
        parser.getDocumentElement().normalize();

        return parser;
    }
}
