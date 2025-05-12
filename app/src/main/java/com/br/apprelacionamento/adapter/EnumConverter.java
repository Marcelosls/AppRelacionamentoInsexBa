package com.br.apprelacionamento.adapter;

public class EnumConverter {

    public static String convertDesiredRelationship(String value) {
        switch (value) {
            case "Namoro": return "Namoro";
            case "Casamento": return "Casamento";
            case "Relação_Aberta": return "Relação Aberta";
            case "Amizade": return "Amizade";
            default: return "Não especificado";
        }
    }

    public static String convertEducation(String value) {
        switch (value) {
            case "Ensino_Fundamental": return "Ensino Fundamental";
            case "Ensino_Médio": return "Ensino Médio";
            case "Ensino_Superior": return "Ensino Superior";
            case "Pós_graduação": return "Pós-graduação";
            default: return "Não informado";
        }
    }

    public static String convertEthnicity(String value) {
        switch (value) {
            case "Branco": return "Branco(a)";
            case "Preto": return "Preto(a)";
            case "Pardo": return "Pardo(a)";
            case "Asiatico": return "Asiático(a)";
            case "Indigena": return "Indígena";
            case "Outro": return "Não informado";
            default: return "Não informado";
        }
    }

    public static String convertMaritalStatus(String value) {
        switch (value) {
            case "Solteiro": return "Solteiro(a)";
            case "Casado": return "Casado(a)";
            case "Divorciado": return "Divorciado(a)";
            case "Viuvo": return "Viúvo(a)";
            default: return "Não informado";
        }
    }

    public static String convertGender(String value) {
        switch (value) {
            case "Masculino": return "Masculino";
            case "Feminino": return "Feminino";
            case "Nao_Binario": return "Não Binário";
            default: return "Não informado";
        }
    }
}

