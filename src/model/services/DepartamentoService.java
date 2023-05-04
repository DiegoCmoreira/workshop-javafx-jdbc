package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Departamento;

public class DepartamentoService {
	
	public List<Departamento> findAll(){
		List<Departamento> list = new ArrayList();
		list.add(new Departamento(1, "livro"));
		list.add(new Departamento(2, "computador"));
		list.add(new Departamento(3, "eletronico"));
		
		return list;
	}
}
