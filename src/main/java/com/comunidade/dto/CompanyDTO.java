package com.comunidade.dto;

import java.io.Serializable;


public class CompanyDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	
	private Integer id;
	
	private String name;	
    
	private String cnpj;		
    
	private String email;		

	private String password;

	private String phone;		
	
	private String razaoSocial;		

	private String fantasia;
    
    
	public CompanyDTO() {
	}

    


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompanyDTO other = (CompanyDTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}



    

    /**
     * @return Integer return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return String return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return String return the cnpj
     */
    public String getCnpj() {
        return cnpj;
    }

    /**
     * @param cnpj the cnpj to set
     */
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    /**
     * @return String return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return String return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return String return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return String return the razaoSocial
     */
    public String getRazaoSocial() {
        return razaoSocial;
    }

    /**
     * @param razaoSocial the razaoSocial to set
     */
    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    /**
     * @return String return the fantasia
     */
    public String getFantasia() {
        return fantasia;
    }

    /**
     * @param fantasia the fantasia to set
     */
    public void setFantasia(String fantasia) {
        this.fantasia = fantasia;
    }

    



    public CompanyDTO(Integer id, String name, String cnpj, String email, String password, String phone,
            String razaoSocial, String fantasia) {
        this.id = id;
        this.name = name;
        this.cnpj = cnpj;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.razaoSocial = razaoSocial;
        this.fantasia = fantasia;
    }

}
