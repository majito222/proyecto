import { Injectable } from '@angular/core';
import { TipoSolicitud } from '../modelos/solicitudes';

export interface ContextoSolicitudIa {
  tipo: TipoSolicitud;
  cancelacion: 'MATERIA' | 'SEMESTRE';
  materia: string;
  grupo: string;
  institucionOrigen: string;
  periodo: string;
}

@Injectable({ providedIn: 'root' })
export class AsistenteIaService {
  responder(pregunta: string, contexto: ContextoSolicitudIa): string {
    const texto = pregunta.toLowerCase();

    if (!pregunta.trim()) {
      return 'Escribe una duda sobre tu solicitud y te ayudo a completarla.';
    }

    if (texto.includes('homolog')) {
      return 'Para una homologacion indica la materia que quieres homologar, donde la cursaste, el periodo academico y por que consideras que corresponde con tu plan de estudios.';
    }

    if (texto.includes('cancel')) {
      return contexto.cancelacion === 'SEMESTRE'
        ? 'Para cancelar semestre explica el motivo general, el periodo academico y si tienes soportes. La solicitud queda asociada al perfil con el que ingresaste.'
        : 'Para cancelar una materia escribe el nombre exacto de la asignatura, grupo si lo sabes, periodo academico y motivo de cancelacion.';
    }

    if (texto.includes('cupo')) {
      return 'Para solicitar cupo escribe la materia, grupo u horario deseado, periodo academico y la razon por la que necesitas el cupo.';
    }

    if (texto.includes('registro') || texto.includes('inscribir')) {
      return 'Para registro de asignatura indica la materia, grupo, horario si lo conoces y el problema que aparece en el sistema.';
    }

    if (texto.includes('descripcion') || texto.includes('llenar') || texto.includes('escribir')) {
      return this.generarDescripcion(contexto);
    }

    return 'Te recomiendo escribir: que tramite necesitas, materia o semestre relacionado, periodo academico, motivo principal y cualquier soporte que tengas.';
  }

  generarDescripcion(contexto: ContextoSolicitudIa): string {
    if (contexto.tipo === 'HOMOLOGACION') {
      return `Solicito revisar la homologacion de la materia ${contexto.materia || '[nombre de la materia]'} cursada en ${contexto.institucionOrigen || '[institucion o programa de origen]'} durante el periodo ${contexto.periodo || '[periodo academico]'}. Deseo que se valide si puede ser reconocida dentro de mi plan de estudios.`;
    }

    if (contexto.tipo === 'SOLICITUD_CUPO') {
      return `Solicito cupo para la materia ${contexto.materia || '[nombre de la materia]'}${contexto.grupo ? ` en el grupo ${contexto.grupo}` : ''} para el periodo ${contexto.periodo || '[periodo academico]'}. Necesito cursarla para continuar con mi avance academico.`;
    }

    if (contexto.tipo === 'REGISTRO_ASIGNATURA') {
      return `Solicito apoyo para registrar la materia ${contexto.materia || '[nombre de la materia]'}${contexto.grupo ? ` en el grupo ${contexto.grupo}` : ''} durante el periodo ${contexto.periodo || '[periodo academico]'}. El sistema no me permite completar el registro correctamente.`;
    }

    if (contexto.tipo === 'CANCELACION' && contexto.cancelacion === 'SEMESTRE') {
      return `Solicito la cancelacion del semestre correspondiente al periodo ${contexto.periodo || '[periodo academico]'}. Presento esta solicitud por motivos personales/academicos y requiero que sea revisada por la dependencia correspondiente.`;
    }

    if (contexto.tipo === 'CANCELACION') {
      return `Solicito cancelar la materia ${contexto.materia || '[nombre de la materia]'}${contexto.grupo ? ` del grupo ${contexto.grupo}` : ''} para el periodo ${contexto.periodo || '[periodo academico]'}. El motivo de la solicitud es personal/academico y requiero revision del caso.`;
    }

    return `Solicito orientacion sobre mi caso academico para el periodo ${contexto.periodo || '[periodo academico]'}. Requiero informacion sobre el proceso, requisitos y pasos a seguir.`;
  }
}
