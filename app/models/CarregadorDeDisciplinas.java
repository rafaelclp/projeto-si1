package models;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.PersistenceException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Responsável por carregar as disciplinas dos arquivos, armazená-las no bd
 * e manter um cache para o caso de múltiplas requisições da lista.
 */
public class CarregadorDeDisciplinas {

	private static Map<String, List<Disciplina>> cache = new TreeMap<String, List<Disciplina>>();
	
	/**
	 * Limpa o cache.
	 */
	public static void limparCache() {
		cache = new TreeMap<String, List<Disciplina>>();
	}
    
    /**
     * Carrega disciplinas do arquivo a partir do tipo de grade
     * 
     * @param tipoDeGrade
     * 			Tipo de grade do qual serão carregadas as disciplinas
     * @return Lista das disciplinas contidas no arquivo XML
     */
    public static List<Disciplina> carregaDisciplinas(TipoDeGrade tipoDeGrade) {
    	String arquivo = obterArquivoCorrespondente(tipoDeGrade);
    	Long idBase = obterIdBase(tipoDeGrade);
    	return carregaDisciplinas(arquivo, idBase);
    }

	/**
	 * Carrega disciplinas e retorna lista com todas as
     * disciplinas contidas no arquivo XML. Se já tiverem sido
     * carregadas anteriormente, apenas as retorna da memória.
     * 
	 * @param arquivo XML de onde serão lidas as disciplinas.
	 * @param idBase Valor numérico a ser usado como o 0 para os IDs, sendo
	 * 			somado ao id de cada disciplina.
	 * @return Lista das disciplinas contidas no arquivo XML
	 */
    private static List<Disciplina> carregaDisciplinas(String arquivo, Long idBase) {
    	if (!cache.containsKey(arquivo)) {
    		List<Disciplina> disciplinas = carregaDisciplinasDoArquivo(arquivo);
    		for (Disciplina d : disciplinas) {
    			d.setId(d.getId() + idBase);
    		}
    		cache.put(arquivo, disciplinas);
    		carregaNoBancoDeDados(disciplinas, true);
    	}
    	return cache.get(arquivo);
    }

    /**
     * Obtém a base(0) dos ids de certo fluxograma. Este id deve ser somado
     * ao id de cada disciplina lida do arquivo para obter o id final.
     * 
     * A finalidade é não ter que se preocupar com isso no XML (disciplinas de
     * diferentes fluxogramas em conflito por id repetido).
     * 
     * @param tipoDeGrade Tipo de grade para o qual se quer o idBase.
     * @return O idBase.
     */
    private static Long obterIdBase(TipoDeGrade tipoDeGrade) {
    	Long idBase = null;
    	if (tipoDeGrade == TipoDeGrade.FLUXOGRAMA_OFICIAL) {
    		idBase = 0L;
    	} else if (tipoDeGrade == TipoDeGrade.FLUXOGRAMA_MAIS_COMUMENTE_PAGO) {
    		idBase = 200L;
    	} else if (tipoDeGrade == TipoDeGrade.FLUXOGRAMA_VIGENTE_APOS_REFORMA) {
    		idBase = 400L;
    	}
    	return idBase;
    }

    /**
     * Obtém o diretório/nome do arquivo correspondente ao tipoDeGrade.
     * 
     * @param tipoDeGrade Tipo de grade para o qual se quer o arquivo.
     * @return Diretório/nome do arquivo.
     */
    private static String obterArquivoCorrespondente(TipoDeGrade tipoDeGrade) {
    	String arquivo = null;
    	if (tipoDeGrade == TipoDeGrade.FLUXOGRAMA_OFICIAL) {
    		arquivo = "conf/res/fluxogramaOficial.xml";
    	} else if (tipoDeGrade == TipoDeGrade.FLUXOGRAMA_MAIS_COMUMENTE_PAGO) {
    		arquivo = "conf/res/fluxogramaMaisComumentePago.xml";
    	} else if (tipoDeGrade == TipoDeGrade.FLUXOGRAMA_VIGENTE_APOS_REFORMA) {
    		arquivo = "conf/res/fluxogramaAposReforma.xml";
    	}
    	return arquivo;
    }

    /**
	 * Carrega disciplinas no banco de dados.
     * 
	 * @param disciplinas Lista de disciplinas a serem carregadas.
	 * @param forcarUpdate Forçar o update caso as disciplinas já estejam no BD?
	 */
    private static void carregaNoBancoDeDados(List<Disciplina> disciplinas, boolean forcarUpdate) {
    	boolean atualizarRequisitos = true;

    	for (Disciplina d : disciplinas) {
    		Disciplina tmp = new Disciplina(d.getNome(), d.getCreditos(), d.getDificuldade(), d.getPeriodoPrevisto(), d.getId());

    		try {
    			// salva os atributos diretos (da tabela disciplina)
    			tmp.save();
    		} catch (PersistenceException e) {
    			// disciplina já no banco de dados, atualiza
    			if (!forcarUpdate) {
    				atualizarRequisitos = false;
    				break;
    			}
    			tmp.update();
    		}
    	}

    	if (atualizarRequisitos) {
	    	for (Disciplina d : disciplinas) {
	    		// salva as relações Disciplina -> Disciplina (pre/pos-requisitos)
	    		d.update();
	    	}
    	}
    }
    
    /**
     * Preenche e retorna lista das disciplinas contidas no arquivo XML
     * 
     * @param arquivo
     * 			XML do qual serão lidas as disciplinas
     * @return Lista das disciplinas carregadas
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
     * Cria disciplina com os dados retirados de um Element cadeira presente
     * no arquivo XML
     * 
     * @param disciplinas
     * 			Lista de disciplinas ja carregadas
     * @param nodeDisciplina
     * 			No XML que representa a cadeira a ser criada
     * @return Disciplina criada a partir do no
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
                periodo, new Long(id));

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
