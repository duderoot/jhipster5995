/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { CfpTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { SpeackerDetailComponent } from '../../../../../../main/webapp/app/entities/speacker/speacker-detail.component';
import { SpeackerService } from '../../../../../../main/webapp/app/entities/speacker/speacker.service';
import { Speacker } from '../../../../../../main/webapp/app/entities/speacker/speacker.model';

describe('Component Tests', () => {

    describe('Speacker Management Detail Component', () => {
        let comp: SpeackerDetailComponent;
        let fixture: ComponentFixture<SpeackerDetailComponent>;
        let service: SpeackerService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [CfpTestModule],
                declarations: [SpeackerDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    SpeackerService,
                    JhiEventManager
                ]
            }).overrideTemplate(SpeackerDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SpeackerDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SpeackerService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Speacker(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.speacker).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
