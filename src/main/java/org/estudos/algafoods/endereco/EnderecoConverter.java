package org.estudos.algafoods.endereco;

public class EnderecoConverter {

    public EnderecoConverter() {
    }

    public static EnderecoDto toEnderecoDto(Endereco endereco) {
        return new EnderecoDto(
                endereco.getCep(),
                endereco.getLogradouro(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getBairro(),
                endereco.getCidade(),
                endereco.getEstado());
    }

    public static Endereco toEnderecoEntity(EnderecoDto enderecoDto) {
        Endereco endereco = new Endereco();
        endereco.setCep(enderecoDto.cep());
        endereco.setLogradouro(enderecoDto.logradouro());
        endereco.setNumero(enderecoDto.numero());
        endereco.setComplemento(enderecoDto.complemento());
        endereco.setBairro(enderecoDto.bairro());
        endereco.setCidade(enderecoDto.cidade());
        endereco.setEstado(enderecoDto.estado());
        return endereco;
    }
}
