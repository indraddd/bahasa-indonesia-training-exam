package com.indraddd.ujianbin;

public class Quiz {
	
	//private variables
	private int _id;
	private String pertanyaan;
	private String jawaban_a;
	private String jawaban_b;
	private String jawaban_c;
	private String jawaban_d;
	private String jawaban_e;
	private String jawaban_bnr;
	
	
	//empty constructor
	public Quiz(){
		_id=0;
		pertanyaan="";
		jawaban_a="";
		jawaban_b="";
		jawaban_c="";
		jawaban_d="";
		jawaban_e="";
		jawaban_bnr="";
		
	}
	
	//constructor
	public Quiz(int id, String pertanyaan, String jawaban_a, String jawaban_b, String jawaban_c, String jawaban_d, String jawaban_e, String jawaban_bnr){
		this._id = id;
		this.pertanyaan = pertanyaan;
		this.jawaban_a = jawaban_a;
		this.jawaban_b = jawaban_b;
		this.jawaban_c = jawaban_c;
		this.jawaban_d = jawaban_d;
		this.jawaban_e = jawaban_e;
		this.jawaban_bnr = jawaban_bnr;
	}
	
	//get ID
	public int getId(){
		return _id;
	}
	
	//set ID
	public void setId(int id){
		this._id = id;
	}
	
	public String getPertanyaan(){
		return pertanyaan;
	}
	
	public void setPertanyaan(String pertanyaan){
		this.pertanyaan = pertanyaan;
	}
	
	public String getJawaban_a(){
		return jawaban_a;
	}
	
	public void setJawaban_a(String jawaban_a){
		this.jawaban_a = jawaban_a;
	}
	
	public String getJawaban_b(){
		return jawaban_b;
	}
	
	public void setJawaban_b(String jawaban_b){
		this.jawaban_b = jawaban_b;
	}
	
	public String getJawaban_c(){
		return jawaban_c;
	}
	
	public void setJawaban_c(String jawaban_c){
		this.jawaban_c = jawaban_c;
	}
	
	public String getJawaban_d(){
		return jawaban_d;
	}
	
	public void setJawaban_d(String jawaban_d){
		this.jawaban_d = jawaban_d;
	}
	
	public String getJawaban_e(){
		return jawaban_e;
	}
	
	public void setJawaban_e(String jawaban_e){
		this.jawaban_e = jawaban_e;
	}
	
	public String getJawaban_bnr(){
		return jawaban_bnr;
	}
	
	public void setJawaban_bnr(String jawaban_bnr){
		this.jawaban_bnr = jawaban_bnr;
	}


}
