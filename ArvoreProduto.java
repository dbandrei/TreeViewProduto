
package arvoreproduto;


import java.util.Arrays;
import java.util.List;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author Andrei
 */
public class ArvoreProduto extends Application {
    
    List<Produto> produtos = Arrays.<Produto> asList(
            //Perfumaria
            new Produto("Maculino", "Perfumaria"),
            new Produto("Infantil", "Perfumaria"),
            new Produto("Feminina", "Perfumaria"),
            
            //Maquiagem
            new Produto("Olhos", "Maquiagem"), 
            new Produto("Labios", "Maquiagem"),
            new Produto("Rosto", "Maquiagem"),
            new Produto("Unhas", "Maquiagem"),
            new Produto("Labios", "Maquiagem"),
            new Produto("Acessorios", "Maquiagem"));
    
    TreeItem<String> rootNode;
    
    //Construtor
    public ArvoreProduto(){
        this.rootNode = new TreeItem<>("Categorias");
    }
    
    @Override
    public void start(Stage stage) {
        rootNode.setExpanded(true);
        for(Produto produto: produtos){
            TreeItem<String> prodFolha = new TreeItem<>(produto.getNome());
            boolean achou = false;
            for(TreeItem<String> depNode : rootNode.getChildren()) {
                if(depNode.getValue().contentEquals(produto.getCategoria())) {
                    depNode.getChildren().add(prodFolha);
                    achou = true;
                    break;
                }
            }
            if(!achou) {
                TreeItem<String> depNode = new TreeItem<>(produto.getCategoria());
                rootNode.getChildren().add(depNode);
                depNode.getChildren().add(prodFolha);
            }
        }
        
        stage.setTitle("Categorias de Produtor (TreeView)");
        VBox vbox = new VBox();
        final Scene scene = new Scene(vbox, 400, 300);
        scene.setFill(Color.LIGHTGRAY);
        
        TreeView<String> treeView = new TreeView<>(rootNode);
        treeView.setEditable(true);
        treeView.setCellFactory((TreeView<String> p) ->
            new TextFieldTreeCellImpl());
        vbox.getChildren().add(treeView);
        stage.setScene(scene);
        stage.show();
    }

    //modificacao das celulas com textfield
    private final class TextFieldTreeCellImpl extends TreeCell<String> {
        
        private TextField textField;
        
        //Construtor
        public TextFieldTreeCellImpl(){
        }
        
        //Medoto para comecar a edicao na celula
        @Override
        public void startEdit() {
            super.startEdit();
            
            //verifica se possui texto na celula
            if(textField == null) {
                createTextField(); //caso nao tenha, se cria
            }
            
            setText(null);
            setGraphic(textField);
            textField.selectAll();
        }
        
        @Override
        public void cancelEdit(){
            super.cancelEdit();
            setText((String) getItem());
            setGraphic((getTreeItem().getGraphic()));
        }
        
        //Relaciona a celula com a possivel agregacao de texto
        @Override
        public void updateItem(String item, boolean vazio){
            super.updateItem(item, vazio);
            
            //atribuindo a string ao textfild
            if(vazio) {
                setText(null);
                setGraphic(null);
            } else {
                if(isEditing()){
                    if(textField != null){
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField); 
                } else {
                    setText(getString());
                    setGraphic(getTreeItem().getGraphic()); //parte visual
                }
            }
        }
        
        //Geracao celula em forma de texto para edicao
        private void createTextField() {
            textField = new TextField(getString());
            
            //Evento de liberacao do teclado
            textField.setOnKeyReleased((KeyEvent t) -> {
                if(t.getCode() == KeyCode.ENTER) {
                    commitEdit(textField.getText()); //captura o texto(celula)
                } else if (t.getCode() == KeyCode.ESCAPE) {
                    cancelEdit(); //acaba com o editar
                }
            });
        }
        
        //Verificacao da armazenagem do item
        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }
    
    public static class Produto{
        private final SimpleStringProperty nome;
        private final SimpleStringProperty categoria;
        
        //Construtor
        private Produto(String nome, String categoria){
            this.nome = new SimpleStringProperty(nome);
            this.categoria = new SimpleStringProperty(categoria);
        }
        
        //Get's + Set's
        public String getNome(){
            return nome.get();
        }
        
        public void setNome(String nome){
            this.nome.set(nome);
        }
        
        public String getCategoria(){
            return categoria.get();
        }
        
        public void setCategoria(String capegoria){
            this.categoria.set(capegoria);
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
