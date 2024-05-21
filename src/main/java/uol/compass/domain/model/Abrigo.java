package uol.compass.domain.model;

public class Abrigo {

    private Integer id;
    private String nome;
    private String endereco;
    private String cep;
    private String cidade;
    private String responsavel;
    private String telefone;
    private String email;
    private Integer capacidade;
    private Double ocupacao;

    public Abrigo() {

    }

    public Abrigo(Integer id, String nome, String endereco, String cep, String cidade, String responsavel, String telefone, String email, Integer capacidade, Double ocuupacao) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.cep = cep;
        this.cidade = cidade;
        this.responsavel = responsavel;
        this.telefone = telefone;
        this.email = email;
        this.capacidade = capacidade;
        this.ocupacao = ocuupacao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(Integer capacidade) {
        this.capacidade = capacidade;
    }

    public Double getOcupacao() {
        return ocupacao;
    }

    public void setOcupacao(Double ocupacao) {
        this.ocupacao = ocupacao;
    }

    @Override
    public String toString() {
        return "Abrigo{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", endereco='" + endereco + '\'' +
                ", responsavel='" + responsavel + '\'' +
                ", telefone='" + telefone + '\'' +
                ", email='" + email + '\'' +
                ", capacidade=" + capacidade +
                ", ocupacao=" + ocupacao +
                '}';
    }
}
