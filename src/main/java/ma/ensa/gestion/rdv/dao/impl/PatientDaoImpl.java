package ma.ensa.gestion.rdv.dao.impl;

import ma.ensa.gestion.rdv.dao.PatientDao;
import ma.ensa.gestion.rdv.model.Patient;

public class PatientDaoImpl extends GenericDaoImpl<Patient, Long> implements PatientDao {

    public PatientDaoImpl() {
        super(Patient.class);
    }
}
